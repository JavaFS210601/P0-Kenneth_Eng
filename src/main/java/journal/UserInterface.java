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
import java.util.Scanner;

import javax.print.attribute.standard.OutputDeviceAssigned;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * User Interface of the command line journal program.
 * @author Kenneth Eng
 *
 */
public class UserInterface {
	JournalDao dao = new JournalDao();
	ArrayList<Journal> journals = new ArrayList<Journal>();
	Boolean isRunning = true;
	
	public static Logger log = LogManager.getLogger( UserInterface.class);
	
	
	public static void main(String[] args) {
		log.info("This is an Journal Application!");
		// log.error("This is an ERROR level log message!");
		
		//Intantiate a postgreSQL connection
		PostgreSQLConnect pc = PostgreSQLConnect.getInstacne();
		UserInterface menu = new UserInterface();
		try {
	
			Connection conn = pc.getConnection();
			menu.Render();
			
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("Program terminated");
		}
	
	}

	
	/*
	 * Method that render the program command line UI 
	 */
	public void Render() {
		System.out.println("                          Welcome to Yinkin Journal                          ");
		System.out.println("---------------------------------- Archive ----------------------------------");
		Scanner sc = new Scanner(System.in);
		while(isRunning) {
			try {
				//mainMenu();
				
				listArticleMenu(sc);	
				
			} catch(UserInputException ue) { 
				log.error(ue.getMessage());
				
				//System.out.println("InValid Input. Please try again.\n");
				System.out.println("|||||||||||||||| New ||||||||||| Ticket ||||||||||||||||");
				System.out.println("\n");
			} catch(SQLException s) {
				log.error(s.getMessage());
				s.printStackTrace();
				sc.close();
				System.exit(0);
			}	
			catch(Exception e) {
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
	private void loadAllArticles() throws SQLException {
		
		journals = dao.retrieveAllJournals();
	
	}
	

	/*
	 * Method that load number of the articles in the database
	 * @param number_of_articles: use enter the number of article 
	 */
	private void loadArticles(int number_of_articles) throws Exception {
//		if (dao.getAuthors().containsKey("kenneth eng")) {
//			System.out.println(" list of authors:"  + dao.getAuthors());
//		}
		
		journals = dao.retrieveJournals(number_of_articles);
		if (journals == null) {
			throw new Exception("the library is empty");
		}
	}
	
	/*
	 * Method that displays options for user to choose from
	 */
	private void EditMenu() throws UserInputException {
		
	}
	
	
	private void listArticleMenu(Scanner sc) throws Exception {
		//loadAllArticles();
		
		loadArticles(5);
		
		System.out.println("Number of Journals :  " + journals.size() );
		for (int i = 0; i < journals.size(); i++) {			
			System.out.print(i+1 + ". "  + journals.get(i).getTitle() + " ");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("***Please type in the number of the article***");
		System.out.println("***type 'add' to insert record with local file***");
		System.out.println("***type 'create' to make a record with command line***");
		System.out.println("***type 'delete' to delete a record ***");

		if(sc.hasNextInt()) {
			int input = sc.nextInt(); 
			if (input >= 1 && input <= journals.size()) {
				displayArticle(input);
			} else {
				log.warn("User enter " + input + " and cause problem");
				
				throw new UserInputException("Input is not within range of 1 to 5");
			}

		} else if (sc.hasNext() ) {
			String input = sc.next(); 
			
			switch (input) {
				case "add": addArticleSubMenu(); 
					break;
				case "create": createArticle(); 
					break;
				case "delete":  removeArticle();
					break;
				default:throw new UserInputException("user enter wrong string keywords:" + input);
			}

			
		} 
		
		exitMenu();
		clearScreen();
	}
	
	

	/*
	 * Method that prompt user for inputs to create article on command line interface
	 */
	private void createArticle() throws SQLException{
			Scanner sc = new Scanner(System.in);
			//System.out.println(dao.getAuthors());
			System.out.println("|\t You can create new article here "+ "\t|");
			
			System.out.println("What is the title of the article ");
			String title  = sc.nextLine();
			System.out.println("Whos is the firstname of author ");
			String firstname = sc.nextLine();
			System.out.println("Whos is the lastname of author ");
			String lastname = sc.nextLine();
			String author = firstname + " " + lastname;
			
			System.out.println("Please enter some text ");
			String content = sc.nextLine();
			//addArticleSubMenu(sc);
			System.out.println("Please choose from one of following category");
			System.out.println( dao.getAllCategory());
			int category = sc.nextInt();
			
			Date today = new Date();
			Journal j = new Journal(title,  author, today , content, category );
			dao.insertJournal(j) ;
			//dao.insertAuthor(firstname.toLowerCase(), lastname.toLowerCase());
			System.out.println("---------------------------------- article added ----------------------------------");
	
			
	}
	
	/*
	 * Method that ask user for the local file path and call the readfile() method to 
	 * attempt to create file.
	 */
	private void addArticleSubMenu() throws UserInputException , SQLException{
			Scanner sc = new Scanner(System.in);
			System.out.println("Please enter the text file path:");
			
			String path = sc.next();
			
			if (!path.isBlank()) {
				StringBuilder output= readFile(path);
				if (output != null) {
					String out = output.toString();
					Date today = new Date();
					Journal j = new Journal("",  "", today , out, 1 );
					dao.insertJournal(j);
				}
			} else {
				
				throw new UserInputException("user enter wrong file path");
				
			}	
			
	}
	
	/*
	 * Method that read local file 
	 * 
	 * The method will handle the file Exceptions on its own 
	 * 
	 * @param path: the file path
	 */
	private StringBuilder readFile(String path) {
		//path = "C:\\Users\\ocean\\Documents\\revature\\ben_section\\proj1\\article0.txt";
		
		StringBuilder output = new StringBuilder();
		
		 File file = new File(path);
		 try {
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  
			  String st;
			  System.out.println("---------------------------------- New Article ----------------------------------");
			  while ((st = br.readLine()) != null) {
			    System.out.println(st);
			    output.append(st+ "\n");
			  }
			  System.out.println("---------------------------------- article added ----------------------------------");
				
		 } catch(Exception e) { 
			 log.error( e.getMessage());
			 System.out.println("file doens't exist");
		 }
		 return output;
	}

	public void removeArticle() throws SQLException{
		System.out.println("Which one do you want to delete?");
		Scanner sc = new Scanner(System.in);
		int id = sc.nextInt() - 1 ;
		dao.deleteJournal(journals.get(id));
	}
	
	/*
	 * Method that displays restart/quit options for user to choose from
	 */
	private void exitMenu() {
		System.out.println("***Hit enter to continue or type 'exit' to quit***");
		Scanner sc2 = new Scanner(System.in);
		String nextInput = sc2.nextLine();
		if (nextInput.isEmpty()) {
			System.out.println("|||||||||||||||| New ||||||||||| Ticket ||||||||||||||||");
			System.out.println("\n");
		} else if(nextInput.toLowerCase().contains("exit") ) {
				isRunning = false;
				System.out.println("Program terminated");
		} else {
			System.out.println("|||||||||||||||| New ||||||||||| Ticket ||||||||||||||||");
			System.out.println("\n");
		};
		
		 
	}
	
	
	/*
	 * Method that displays the article retrieved from postgreSQL database
	 *
	 * @param userInput: an integer value which entered by the user to decide which 
	 * article the method will retrieve.
	 */
	private void displayArticle(int userInput) throws SQLException {
		//Journal j = dao.getJournalById(userInput-1);
		
		Journal j = dao.getJournalById(userInput-1);
		System.out.println(" -------------------------------------------- ||  --------------------------------------------  \r");
		System.out.println("|\t title: " + j.getTitle() + "\t|");
		System.out.println("|\t author: " + j.getAuthor() + "\t|");
		System.out.println("|\t date: " + j.getCreatedDate() + "\t|");
		try {
			int l = j.getArticle().length();
			System.out.println("\t" + j.getArticle() + " ");
			//System.out.println("");
			//System.out.println("\t" +j.getArticle()[1] + " ");
			
			System.out.println(" -------------------------------------------- ||  --------------------------------------------  \r");
		} catch(IndexOutOfBoundsException e) {
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
		//System.out.print("\033\143");
	}  
}
