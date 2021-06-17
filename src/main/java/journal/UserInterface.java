/**
 * 
 */
package journal;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author ocean
 *
 */
public class UserInterface {
	JournalDao dao = new JournalDao();
	Journal[] journals;
	Boolean isRunning = true;
	
	public static Logger log = LogManager.getLogger( UserInterface.class);
	public static void main(String[] args) {
//		log.info("This is an INFO level log message!");
//		 log.error("This is an ERROR level log message!");
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
		System.out.println("          Welcome to Yinkin Journal          ");
		System.out.println("--------------- Archive ---------------");
		while(isRunning) {
			try {
				mainMenu();

			} catch(UserInputException ue) { 
				System.out.println("InValid Input. Please try again.\n");
				System.out.println("|||||||||||||||| New ||||||||||| Ticket ||||||||||||||||");
				System.out.println("\n");
			} 
		}
		
	}
	
	private void loadAllArticles() {
		journals = dao.retrieveAllJournals();
		
	}
	
	private void mainMenu() throws UserInputException {
		
		
		
		loadAllArticles();
		for (int i = 0; i < journals.length; i++) {			
			System.out.println("Journal "  + journals[i].getTitle());
		}
		
		System.out.println("Please type in the number of the following article");
		Scanner sc = new Scanner(System.in);

		if(sc.hasNextInt()) {
			int input = sc.nextInt(); 
			if (input >= 1 && input <= 5) {
				displayArticle(input);
			} else {
				 log.error("User enter wrong inputs!");
				throw new UserInputException("Input is not within range of 1 to 5");
			}
			
		}
		
		subMenu();
		//clearScreen();
	}
	public static void clearScreen() {  
//	    System.out.print("\033[H\033[2J");  
//	    System.out.flush();
		//System.out.print("\033\143");
	}  
	
	private void subMenu() {
		System.out.println("   Hit enter to continue or type exit to quit    ");
		Scanner sc = new Scanner(System.in);
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
	
	private void displayArticle(int userInput) {
		Journal j = dao.getJournalById(userInput-1);
		
		System.out.println("| title: " + j.getTitle() + " |");
		System.out.println("| author: " + j.getAuthor() + " |");
		System.out.println("| date: " + j.getCreatedDate() + " |");
		System.out.println("|\t" + j.getArticle()[0] + " |");
		System.out.println("");
		System.out.println("|\t" +j.getArticle()[1] + " |");
		System.out.println(" -------------------------------------------- \r ");
	}

}
