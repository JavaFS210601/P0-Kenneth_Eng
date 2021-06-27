package journal;
import static org.junit.jupiter.api.Assertions.*; //these are for your assert method
/*
 *  TDD test driven developer
 *  
 *  Plan:
 *  Networking: 
 * 
 *  UI: 
 *  
 *  Model: 
 *  Check if the getters return correct data
 *  
 *  Logic:
 *  
 */

import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class unitTest {

	public static Journal j;
	public static UserInterface ui;
	public static JournalDao dao;
	
	// Journal test values
	public String author;

	public String result;

	//the @Before/After methods are just to help set up for our Testing (Assert) methods
	
	@BeforeAll //used for things you want to happen BEFORE the test Class does anything.
	public static void initializeLauncher() {
		System.out.println("In BeforeAll");
		j = new Journal();
		ui = new UserInterface();
		dao = new JournalDao();
	}
	
	@BeforeEach //used for things you want to happen BEFORE EACH testing method
	public void setVariables() {
		author = "Kenneth  Eng";
	}
	
	@AfterEach //Used for things you want to happen AFTER EACH testing method
	public void clearResults() {
		System.out.println("In AfterAll");
		result = "";
	}
	
	@Test 
	public void testJournalMethods() {
		System.out.println("TESTING Journal Object methods...");
		try {
		Journal journal = dao.getJournalById(1);
		result = journal.getAuthor();
		} catch(SQLException s) {
			s.printStackTrace();
		}
		assertEquals(author, result); 
		
		// if assertEquals is true, this line will execute
		System.out.println("Author is myself indeed...");
	}

	@Test 
	public void testUI() {
		System.out.println("TESTING UI...");
		
		//assertThrows(UserInputException.class,  () -> j.displayArticle(1)  )
		
		//assertNotEquals(0, result); 
	}
}