/**
 * 
 */
package journal;

/**
 * Custom Exception is used to represent the user input error
 * @author Kenneth Eng
 *
 */
public class UserInputException extends RuntimeException {

	public UserInputException() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public UserInputException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
		System.out.println(message);
		
	}

	
}
