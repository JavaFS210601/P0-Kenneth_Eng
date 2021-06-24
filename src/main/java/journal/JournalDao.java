package journal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class JournalDao implements JournalDaoImpl {
	private HashMap<Integer,String> listOfCategorys;
	private ArrayList<Journal> listOfJournals;
	
	JournalDao(){
		listOfCategorys = new HashMap<Integer,String>();
		listOfJournals = new ArrayList<Journal>();
			//retrieveAllJournals();
		
	}
	
	JournalDao(int num_of_article){
		listOfJournals = new ArrayList<Journal>();
		//retrieveAllJournals(num_of_article);
	}
	
	@Override
	public void deleteAuthor(int author_id) throws SQLException {
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		String sql = "DELETE FROM authors "
				+ " WHERE author_id = ?; ";
		//Statement s = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, author_id);
		ps.executeUpdate();
	}
	
	@Override
	public void updateAuthor(String firstname, String lastname) throws SQLException {
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		String sql = "UPDATE authors  "
				+ " set author_firstname, author_lastname "
				+ " WHERE author_id = ?; ";
		//Statement s = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, firstname);
		ps.setString(2, lastname);
		ps.executeUpdate();
	}
	
	@Override
	public void insertAuthor(String firstname, String lastname) throws SQLException{
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		String sql = "INSERT INTO authors(author_firstname, author_lastname) "
				+ "VALUES (?,?);";
		//Statement s = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, firstname);
		ps.setString(2, lastname);
		ps.executeUpdate();

	}
	
	public void deleteJournal(Journal journal ) throws SQLException {
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		String sql = "DELETE FROM journals "
				+ " WHERE journal_id = ?; ";
		//Statement s = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setInt(1, journal.getId());
		ps.executeUpdate();
	}
	
	@Override
	public void insertJournal(Journal journal) throws SQLException{
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	

		DateFormat dateformat = new SimpleDateFormat("yyyy-mm-dd");
		//Date date = new Date();
		
		String createDate = dateformat.format(journal.getCreatedDate());
		
		System.out.println("Create Date:" + createDate  + " " + journal.getCategory());
		java.sql.Date date = java.sql.Date.valueOf(createDate);
		String sql = "INSERT INTO journals(journal_title, journal_author, journal_content, journal_create_date, journal_category_fk) "
				+ "VALUES (?,?,?,?,?);";
		Statement s = conn.createStatement();
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, journal.getTitle());
		ps.setString(2, journal.getAuthor());
		ps.setString(3, journal.getArticle());
		ps.setDate(4, date );
		ps.setInt(5, journal.getCategory());
		ps.executeUpdate();

	}
	
	@Override
	public HashMap<Integer, String> getAllCategory() throws SQLException{
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		ResultSet rs = null;
		String sql = "SELECT * FROM categorys";
		Statement s = conn.createStatement();
		rs = s.executeQuery(sql);
		while (rs.next()  ) {
			int i = rs.getInt("category_id");
			String a = rs.getString("category_name");
			listOfCategorys.put(i, a);
	
		}
		return listOfCategorys;
	}
	
	@Override
	public  ArrayList<Journal> retrieveJournals(int number_of_articles) throws SQLException {
		//try (Connection conn = PostgreSQLConnect.getInstacne().getConnection()) {
		Connection conn = PostgreSQLConnect.getInstacne().getConnection();	
		ResultSet rs = null; //initailize an empty set that will store the results of our query
		String sql = "SELECT * FROM journals";
		Statement s = conn.createStatement();
		rs = s.executeQuery(sql);

		//ArrayList<Journal> journalList = new ArrayList<>();
		int i = 0;
		while (rs.next() && i < number_of_articles ) {
			String arr =  rs.getString("journal_content") ;
			Journal j = new Journal(rs.getInt("journal_id"), rs.getString("journal_title"),
					"kenneth eng", rs.getDate("journal_create_date"), arr, 1);
			listOfJournals.add(j);
			
			i++;
			
		}
		//}
		//System.out.println(listOfJournals.toString());
		return listOfJournals;	
	}
	
	
	@Override
	public ArrayList<Journal> retrieveAllJournals() throws SQLException  {
		listOfJournals = new ArrayList<Journal>();
		
		String arr = " At w3schools.com you will learn how to make a website. \n "
				+ "Contented get distrusts certainty nay are frankness concealed ham. \n"
				+ "On unaffected resolution on considered of. No thought me husband or colonel forming effects. \n"
				+ "End sitting shewing who saw besides son musical adapted. Contrasted interested eat \n"
				+ "alteration pianoforte sympathize was. He families believed if no elegance interest surprise an. \n"
				+ "It abode wrong miles an so delay plate. She relation own put outlived may disposed. \n"  
				+"Why end might ask civil again spoil. She dinner she our horses depend. Remember at children \n"
				+ "by reserved to vicinity. In affronting unreserved delightful simplicity ye. Law own advantage \n"
				+ "furniture continual sweetness bed agreeable perpetual. Oh song well four only head busy it. \n"
				+ " Afford son she had lively living. Tastes lovers myself too formal season our valley boy. \n"
				+ "Lived it their their walls might to by young. ";
//		String[] arr1 = {"this is article 1." + arr[0], arr[1]} ; 
//		String[] arr2 = {"this is article 2." + arr[0], arr[1]} ; 
//		String[] arr3 = {"this is article 3." + arr[0], arr[1]} ; 
//		String[] arr4 = {"this is article 4." + arr[0], arr[1]} ;
//		String[] arr5 = {"this is article 5." + arr[0], arr[1]} ; ;
		Journal j =  new Journal(1,"Article 1","Kenneth Eng", new Date(), arr,1);
		Journal j1 =  new Journal(2, "Article 2","Kenneth Eng", new Date(), arr, 1);
		Journal j2 =  new Journal(3, "Article 3","Kenneth Eng", new Date(), arr, 1);
		Journal j3 =  new Journal(4,"Article 4","Kenneth Eng", new Date(), arr, 1);
		Journal j4 =  new Journal(5, "Article 5","Kenneth Eng", new Date(), arr,1 );
		listOfJournals.add(j);
		listOfJournals.add(j1);
		listOfJournals.add(j2);
		listOfJournals.add(j3);
		listOfJournals.add(j4);
		return listOfJournals;
	}
	
	@Override
	public Journal getJournalById(int id) throws SQLException  {
		return listOfJournals.get(id);
	}

	@Override
	public void reset() {
		listOfJournals.clear();
		listOfCategorys.clear();
	}
	

}
