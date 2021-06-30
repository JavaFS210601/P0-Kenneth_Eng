package journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

import javax.print.attribute.standard.OutputDeviceAssigned;
import javax.swing.text.html.HTMLDocument.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jdk.internal.org.jline.utils.Log;

/**
 * User Interface of the command line journal archive program.
 * 
 * @author Kenneth Eng
 *
 */
public class UserInterface {
	private JournalDao dao = new JournalDao();
	private ArrayList<Journal> journals = new ArrayList<Journal>();
	private Boolean isRunning = true;
	private Boolean isFilterByAuthor = false;
	private Boolean isFilterByCategory = false;
	private int filter_value;
	public static Logger log = LogManager.getLogger(UserInterface.class);

	public static void main(String[] args) {
		log.info("Journal Application Starting!");

		// Intantiate a postgreSQL connection
		PostgreSQLConnect pc = PostgreSQLConnect.getInstacne();
		
		UserInterface menu = new UserInterface();
		try {

			menu.Render();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Program terminated");
		}

	}

	/*
	 * Method that render the program command line UI
	 */
	public void Render() {
		System.out.println("                          Welcome to the Journal Archive                         ");
		System.out.println("---------------------------------- START ----------------------------------");
		Scanner sc = new Scanner(System.in);
		while (isRunning) {
			try {

				listArticleMenu(sc);

			} catch (UserInputException ue) {
				log.error(ue.getMessage());
				System.out.println("");
				// System.out.println("InValid Input. Please try again.\n");
				System.out.println("|||||||||||||||| New ||||||||||| Session ||||||||||||||||");
				System.out.println("\n");
			} catch (SQLException s) {
				log.error(s.getMessage());
				s.printStackTrace();
				sc.close();
				System.exit(0);
			} catch (Exception e) {
				log.error(e.getMessage());
				e.printStackTrace();
				sc.close();
				System.exit(0);
			}
		}

		// close all Scanner objects
		sc.close();

	}

	/*
	 * Method that load all of the articles in the database
	 */
	private void loadArticles() throws SQLException {

		journals = dao.retrieveAllJournals();

	}

	/*
	 * Method that load number of the articles in the database
	 * 
	 * @param number_of_articles: use enter the number of article
	 */
	private void loadArticles(int number_of_articles) throws Exception {
		

		journals = dao.retrieveJournals(number_of_articles);
		if (journals == null) {
			throw new Exception("the library is empty");
		}
	}

	/*
	 * Method that displays options for user to choose from
	 */
	private void filterMenu() throws UserInputException, SQLException {
		//boolean isFilter = true;

		System.out.println("***type in 'author' or 'category' to set the filter***");
		Scanner sc = new Scanner(System.in);
		String input = sc.next();
		switch(input) {
			case "author":
				isFilterByCategory = false;
				System.out.println("please choose a author for filtering ");
				//System.out.println(dao.getAuthors().toString());
				HashMap<Integer, String > authorsList = dao.getAuthors();
				for (Map.Entry<Integer, String> element : authorsList.entrySet()) {
					int key = (int)element.getKey();
					System.out.print(key+1 + ":" + element.getValue() + " " );
				}
				System.out.println("" );
				try {
					filter_value = sc.nextInt() - 1; // filter_value is 1 less than the display
					journals = dao.getJournalsByAuthorName(filter_value);
					
				}catch (Exception e) {
					throw new UserInputException("User enter wrong input at filter author");
				}
				System.out.println("filter applied"  );
				if (journals.size() == 0 ) {
					isFilterByAuthor = false;
				} else {
					isFilterByAuthor = true;
				}
				break;
			case "category":
				isFilterByAuthor = false;
				System.out.println("please choose a category for filtering ");
				//System.out.println(dao.getAllCategory().toString());
				HashMap<Integer, String > categorysList = dao.getAllCategory();
				for (Map.Entry<Integer, String> element : categorysList.entrySet()) {
					int key = (int)element.getKey();
					System.out.print(key + ":" + element.getValue() + " " );
				}
				System.out.println("" );
				try {
					filter_value = sc.nextInt(); 
					System.out.println(filter_value );
	 				journals = dao.getJournalsByCategory(filter_value);
				}catch (Exception e) {
					throw new UserInputException("User enter wrong input at filter category");
				}
				System.out.println("filter applied"  );
				
				if (journals.size() == 0 ) {
					isFilterByCategory = false;
				} else {
					isFilterByCategory = true;
				}
			break;
		}
		

	}

	/*
	 * Method that displays list of journals and commands for user to enter
	 */
	private void listArticleMenu(Scanner sc) throws Exception {
		// loadAllArticles();
		clearScreen();
		if (isFilterByCategory) {
			HashMap<Integer, String > categorysList = dao.getAllCategory();
			System.out.println("Filtered by category: " + categorysList.get(filter_value));
			journals = dao.getJournalsByCategory(filter_value);
		} else if (isFilterByAuthor) {
			HashMap<Integer, String > authorsList = dao.getAuthors();
			System.out.println("Filtered by author name: " + authorsList.get(filter_value));
			journals = dao.getJournalsByAuthorName(filter_value);
		} else {
			loadArticles(50);
		}
		System.out.println("Number of Journals :  " + journals.size());
		for (int i = 0; i < journals.size(); i++) {
			System.out.print(i + 1 + ". " + journals.get(i).getTitle() + " ");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("***Please type in the number of the article***");
		System.out.println("***type 'filter' to filter records , unfilter to reset ***");
		System.out.println("***type 'add' to insert record with local file***");
		System.out.println("***type 'create' to make a record with command line***");
		System.out.println("***type 'delete' to delete a record ***");
		System.out.println("***type 'update' to update a record ***");
		System.out.println("***type 'exit' to quit ***");

		if (sc.hasNextInt()) {
			int input = sc.nextInt();
			if (input >= 1 && input <= journals.size()) {
				displayArticle(input);
			} else {
				log.warn("User enter " + input + " and cause problem");

				throw new UserInputException("Input is not within range of 1 to 5");
			}

		} else if (sc.hasNext()) {
			String input = sc.next();
			input.toLowerCase();
			switch (input) {
			case "add":
				addArticleSubMenu();
				break;
			case "create":
				createArticle();
				break;
			case "delete":
				removeArticle();
				break;
			case "update": updateArticle();
				break;
			case "filter": filterMenu();
				break;
			case "unfilter": isFilterByAuthor = false;
							isFilterByCategory = false;
				break;
			case "exit": System.out.println("Program terminated");
						sc.close();
						System.exit(0);
				break;
			default:
				throw new UserInputException("user enter wrong string keywords:" + input);
			}

		}

		exitMenu();
		
	}
	
	/*
	 * Method that prompt user for inputs to update a article on command line
	 * interface
	 */
	private void updateArticle() throws SQLException {
		Scanner sc = new Scanner(System.in);
		int index;
		// System.out.println(dao.getAuthors());
		System.out.println("|\t You can update a article here " + "\t|");
		System.out.println("***Please type in the number of the article***");
		try {
			String input = sc.nextLine();
			index = Integer.valueOf(input);
			//System.out.println(index);
		}catch (Exception e) {
			throw new UserInputException("User enter wrong input at choosing  article to update");
		}
		
		System.out.println("What is the title of the article ");
		String title = sc.nextLine();
		System.out.println("Whos is the firstname of author ");
		String firstname = sc.nextLine();
		System.out.println("Whos is the lastname of author ");
		String lastname = sc.nextLine();
		String author = firstname + " " + lastname;

		System.out.println("Please enter some text ");
		String content = sc.nextLine();
		// addArticleSubMenu(sc);
		System.out.println("Please choose from one of following category");
		System.out.println(dao.getAllCategory());
		int category = sc.nextInt();

		Date today = new Date();
		Journal j =  dao.getJournalById(index-1);
		j.setArticle(content);
		j.setAuthor(author);
		j.setCategory(category);
		j.setTitle(title);
				//new Journal(title, author, today, content, category);
		dao.updateJournal(j);
		// dao.insertAuthor(firstname.toLowerCase(), lastname.toLowerCase());
		System.out.println("---------------------------------- article updated ----------------------------------");

	}

	/*
	 * Method that prompt user for inputs to create a article on command line
	 * interface
	 */
	private void createArticle() throws SQLException {
		Scanner sc = new Scanner(System.in);
		// System.out.println(dao.getAuthors());
		System.out.println("|\t You can create new article here " + "\t|");

		System.out.println("What is the title of the article ");
		String title = sc.nextLine();
		System.out.println("Whos is the firstname of author ");
		String firstname = sc.nextLine();
		System.out.println("Whos is the lastname of author ");
		String lastname = sc.nextLine();
		String author = firstname + " " + lastname;

		System.out.println("Please enter some text ");
		String content = sc.nextLine();
		// addArticleSubMenu(sc);
		System.out.println("Please choose from one of following category");
		System.out.println(dao.getAllCategory());
		int category = sc.nextInt();

		Date today = new Date();
		Journal j = new Journal(title, author, today, content, category);
		dao.insertJournal(j);
		// dao.insertAuthor(firstname.toLowerCase(), lastname.toLowerCase());
		System.out.println("---------------------------------- article added ----------------------------------");

	}

	/*
	 * Method that prompt user for inputs to create a article on command line
	 * interface
	 */
	public void removeArticle() throws SQLException {
		
			System.out.println("Which one do you want to delete?");
			Scanner sc = new Scanner(System.in);
			if (sc.hasNextInt()) {
				int id = sc.nextInt() - 1;
				dao.deleteJournal(journals.get(id));
				journals.remove(id);
			} else {
				log.warn("Unable to delete the article. Please choose from the list.");
			}
			if (journals.size() == 0 ) {
				isFilterByCategory = false;
				isFilterByAuthor = false;
			}
		
	}
	
	/*
	 * Method that ask user for the local file path and call the readfile() method
	 * to attempt to create file.
	 */
	private void addArticleSubMenu() throws UserInputException, SQLException {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("|\t You can create new article here " + "\t|");

		System.out.println("What is the title of the article ");
		String title = sc.nextLine();
		System.out.println("Whos is the firstname of author ");
		String firstname = sc.nextLine();
		System.out.println("Whos is the lastname of author ");
		String lastname = sc.nextLine();
		
		System.out.println("Please choose from one of following category");
		System.out.println(dao.getAllCategory());
		int category = sc.nextInt();
		String author = firstname + " " + lastname;
		
		System.out.println("Please enter the text file path:");
		String path = sc.next();

		if (!path.isBlank()) {
			StringBuilder output = readFile(path);
			if (!output.isEmpty()) {
				String out = output.toString();
				Date today = new Date();
				Journal j = new Journal(title, author, today, out, category);
				dao.insertJournal(j);
			}
		} else {

			throw new UserInputException("user enter wrong file path");

		}

	}

	/*
	 * Method that read local file. it will handle the file Exceptions on its own
	 * 
	 * @param path: the file path
	 */
	private StringBuilder readFile(String path) {
		// path =
		// "C:\\Users\\ocean\\Documents\\revature\\ben_section\\proj1\\article0.txt";

		StringBuilder output = new StringBuilder();

		File file = new File(path);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));

			String st;
			System.out.println("---------------------------------- New Article ----------------------------------");
			while ((st = br.readLine()) != null) {
				System.out.println(st);
				output.append(st + "\n");
			}
			System.out.println("---------------------------------- article added ----------------------------------");

		} catch (Exception e) {
			log.error(e.getMessage());
			System.out.println("file doens't exist");
		}
		return output;
	}


	/*
	 * Method that displays restart/quit options for user to choose from
	 */
	private void exitMenu() {
		System.out.println("***Hit enter to continue or type 'exit' to quit***");
		Scanner sc2 = new Scanner(System.in);
		String nextInput = sc2.nextLine();
		if (nextInput.isEmpty()) {
			System.out.print("");
			System.out.println("|||||||||||||||| New ||||||||||| Session ||||||||||||||||");
			System.out.println("\n");
		} else if (nextInput.toLowerCase().contains("exit")) {
			isRunning = false;
			System.out.println("Program terminated");
		} else {
			System.out.println("");
			System.out.println("|||||||||||||||| New ||||||||||| Session ||||||||||||||||");
			System.out.println("\n");
		}
		;

	}

	/*
	 * Method that displays the article retrieved from postgreSQL database
	 *
	 * @param userInput: an integer value which entered by the user to decide which
	 * article the method will retrieve.
	 */
	private void displayArticle(int userInput) throws SQLException {
		// Journal j = dao.getJournalById(userInput-1);

		Journal j = dao.getJournalById(userInput - 1);
		System.out.println(
				" -------------------------------------------- ||  --------------------------------------------  \r");
		System.out.println("|\t title: " + j.getTitle() + "\t|");
		System.out.println("|\t author: " + j.getAuthor() + "\t|");
		System.out.println("|\t date: " + j.getCreatedDate() + "\t|");
		HashMap<Integer, String> cats = dao.getAllCategory();
		System.out.println("|\t category: " + cats.get(j.getCategory()) + "\t|");
		try {
			int l = j.getArticle().length();
			System.out.println("\t" + j.getArticle() + " ");

			System.out.println(
					" -------------------------------------------- ||  --------------------------------------------  \r");
		} catch (IndexOutOfBoundsException e) {
			log.error(e.getMessage());
		}
	}

	/*
	 * Method that reset the list of articles.
	 */
	public void clearScreen() {
		dao.reset();
//	    System.out.print("\033[H\033[2J");  
//	    System.out.flush();

	}
}
