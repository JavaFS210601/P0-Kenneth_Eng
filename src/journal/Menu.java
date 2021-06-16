/**
 * 
 */
package journal;

/**
 * @author ocean
 *
 */
public class Menu {



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Menu menu = new Menu();
		menu.mainMenu();
	
	}
	
	public void mainMenu() {
		System.out.println("          Welcome Custome A          ");
		subMenu();
	}
	
	private void subMenu() {
		System.out.println("  Choose your favorite Article          ");
	}

}
