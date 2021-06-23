package journal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * User Interface of the command line journal program.
 * @author Kenneth Eng
 *
 */
public class UserInterface {
	JournalDao dao = new JournalDao();
	Journal[] journals;
	Boolean isRunning = true;
	
	public static Logger log = LogManager.getLogger( UserInterface.class);
	
	public static void main(String[] args) {
		log.info("This is an Journal Application!");
		// log.error("This is an ERROR level log message!");
		
		//Intantiate a postgreSQL connection
		PostgreSQLConnect pc = PostgreSQLConnect.getInstacne();
		
		try {
			
			
			UserInterface menu = new UserInterface();
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
				System.out.println("InValid Input. Please try again.\n");
				System.out.println("|||||||||||||||| New ||||||||||| Ticket ||||||||||||||||");
				System.out.println("\n");
			} 
		}
		sc.close();
		
	}
	
	/*
	 * Method that load all of the articles in the database
	 */
	private void loadAllArticles() {
		journals = dao.retrieveAllJournals();
		
	}
	
	/*
	 * Method that displays options for user to choose from
	 */
	private void mainMenu() throws UserInputException {
		
	}
	
	
	private void listArticleMenu(Scanner sc) throws UserInputException {
		loadAllArticles();
		System.out.println("Journals :  " );
		for (int i = 0; i < journals.length; i++) {			
			System.out.print(i+1 + ". "  + journals[i].getTitle() + " ");
		}
		System.out.println("");
		System.out.println("");
		System.out.println("***Please type in the number of the article***");
		System.out.println("***type 'add' to insert record with local file***");
		System.out.println("***type 'create' to make a record with command line***");
		

		if(sc.hasNextInt()) {
			int input = sc.nextInt(); 
			if (input >= 1 && input <= 5) {
				displayArticle(input);
			} else {
				log.warn("User enter " + input + " and cause problem");
				
				throw new UserInputException("Input is not within range of 1 to 5");
			}

		} else if (sc.hasNext() ) {
			String input = sc.next(); 
			if (input.contains("add")) {
				addArticleSubMenu(sc);
			} else if (input.contains("create")) {
				createArticle(sc); 
			} else {
				
				throw new UserInputException("user enter wrong string keywords:" + input);
			}
			
		} 
		
		exitMenu(sc);
		//clearScreen();
	}
	
	

	/*
	 * Method that prompt user for inputs to create article on command line interface
	 */
	private void createArticle(Scanner sc) {
		
			System.out.println("|\t You can create new article here "+ "\t|");
			System.out.println("What is the title of the article ");
			System.out.println("Whos is the author ");
		
	}
	
	/*
	 * Method that ask user for the local file path and call the readfile() method to 
	 * attempt to create file.
	 */
	private void addArticleSubMenu(Scanner sc) throws UserInputException {
		
			System.out.println("Please enter the text file path:");
			
			String path = sc.next();
			
			if (!path.isBlank()) {
				readFile(path);
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
	private void readFile(String path) {
		//path = "C:\\Users\\ocean\\Documents\\revature\\ben_section\\proj1\\article0.txt";
		 File file = new File(path);
		 try {
			  BufferedReader br = new BufferedReader(new FileReader(file));
			  
			  String st;
			  System.out.println("---------------------------------- New Article ----------------------------------");
			  while ((st = br.readLine()) != null) {
			    System.out.println(st);
			  }
			  System.out.println("---------------------------------- article added ----------------------------------");
				
		 } catch(Exception e) { 
			 log.error( e.getMessage());
			 System.out.println("file doens't exist");
		 }
	}

	
	/*
	 * Method that displays restart/quit options for user to choose from
	 */
	private void exitMenu(Scanner sc) {
		System.out.println("***Hit enter to continue or type 'exit' to quit***");
		//Scanner sc = new Scanner(System.in);
		String nextInput = sc.nextLine();
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
	private void displayArticle(int userInput) {
		Journal j = dao.getJournalById(userInput-1);
		
		System.out.println("|\t title: " + j.getTitle() + "\t|");
		System.out.println("|\t author: " + j.getAuthor() + "\t|");
		System.out.println("|\t date: " + j.getCreatedDate() + "\t|");
		try {
			int l = j.getArticle()[0].length();
			System.out.println("\t" + j.getArticle()[0] + " ");
			System.out.println("");
			System.out.println("\t" +j.getArticle()[1] + " ");
			
			System.out.println(" -------------------------------------------- ||  --------------------------------------------  \r");
		} catch(IndexOutOfBoundsException e) {
			log.error(e.getMessage());
		}
	}

	
	
	public static void clearScreen() {  
//	    System.out.print("\033[H\033[2J");  
//	    System.out.flush();
		//System.out.print("\033\143");
	}  
}
