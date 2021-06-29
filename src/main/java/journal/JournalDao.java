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
import java.util.Map.Entry;

public class JournalDao implements JournalDaoImpl {
	private HashMap<Integer, String> listOfCategorys;
	private ArrayList<Journal> listOfJournals;
	private HashMap<Integer, String> listOfAuthors;
	
	JournalDao() {
		listOfCategorys = new HashMap<Integer, String>();
		listOfJournals = new ArrayList<Journal>();
		listOfAuthors = new HashMap<Integer, String>();
		// retrieveAllJournals();

	}

	JournalDao(int num_of_article) {
		listOfJournals = new ArrayList<Journal>();
		// retrieveAllJournals(num_of_article);
	}

	/*
	 * Method that delete a category to category table
	 *
	 * @param category_id: specific the category with id to delete
	 */
	@Override
	public void deleteCategory(int catgory_id)  throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "DELETE FROM categorys " + " WHERE category_id = ?; ";
			// Statement s = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, catgory_id);
			ps.executeUpdate();

		}
	}

	/*
	 * Method that updates a category in the category table 
	 *
	 * @param category_id: specific the category with id to update
	 */
	@Override
	public void updateCategory(int catgory_id) throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "UPDATE  categorys  " + " set category_name " + " WHERE category_id = ?; ";
			// Statement s = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, catgory_id);
			ps.executeUpdate();
		}
	}

	/*
	 * Method that inserts a category from category table 
	 *
	 * @param category_id: specific the category with id to insert
	 */
	@Override
	public void insertCategory(String name) throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "INSERT INTO authors(category_name) " + "VALUES (?);";
			// Statement s = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.executeUpdate();
		}

	}
	
	/*
	 * Method that  retrieves all author name from journals
	 */
	public HashMap<Integer, String> getAuthors() throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "SELECT DISTINCT journal_author FROM journals;";
			ResultSet rs = null;
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			
			int i = 0;
			while (rs.next()) {
				//int i = rs.getInt("journal_author");
				String a = rs.getString("journal_author");
				listOfAuthors.put(i, a);
				i++;
			}
			return listOfAuthors;
		}
	}
	
	/*
	 * Method that  retrieves journal by author name from journals table
	 * 
	 * @param id: specific the id of the author in the list of author 
	 */
	public ArrayList<Journal> getJournalsByAuthorName (int id) throws SQLException {
		//ArrayList<Journal> listOfJournals = new ArrayList<Journal>();
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			ResultSet rs = null;
			String sql = "SELECT * FROM journals WHERE journal_author = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, listOfAuthors.get(id));
			//System.out.println(ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				String arr = rs.getString("journal_content");
				Journal j = new Journal(rs.getInt("journal_id"), rs.getString("journal_title"), rs.getString("journal_author"),
						rs.getDate("journal_create_date"), arr, rs.getInt("journal_category_fk"));
				listOfJournals.add(j);
	
			}
		}
		return listOfJournals ;
	}

	/*
	 * Method that  retrieves journal by category name from journals table
	 * 
	 * @param id: specific the id of the author in the list of category 
	 */
	@Override
	public ArrayList<Journal> getJournalsByCategory (int id) throws SQLException {
	
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			ResultSet rs = null;
			String sql = "SELECT * FROM journals WHERE journal_category_fk = ?;";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, id);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				String arr = rs.getString("journal_content");
				Journal j = new Journal(rs.getInt("journal_id"), rs.getString("journal_title"), rs.getString("journal_author"),
						rs.getDate("journal_create_date"), arr, rs.getInt("journal_category_fk"));
				listOfJournals.add(j);
	
			}
		}
		return listOfJournals ;
	}
	
	/*
	 * Method that  retrieves all category stored in categorys table
	 */
	@Override
	public HashMap<Integer, String> getAllCategory() throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			ResultSet rs = null;
			String sql = "SELECT * FROM categorys";
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			while (rs.next()) {
				int i = rs.getInt("category_id");
				String a = rs.getString("category_name");
				listOfCategorys.put(i, a);
	
			}
		}
		return listOfCategorys;
	}
	
	/*
	 * Method that update a journal in journals table
	 *
	 * @param journal: the journal used to replace the old journal record
	 */
	//@Override
	public void updateJournal(Journal journal) throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "UPDATE journals "
						+ "SET journal_title = ?, journal_author = ?, "
						+ "journal_content = ?, journal_create_date = ?, "
						+ "journal_category_fk = ? " 
						+ "WHERE  journal_id = ?; ";
			
			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
			String createDate = dateformat.format(journal.getCreatedDate());
			java.sql.Date date = java.sql.Date.valueOf(createDate);
			
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, journal.getTitle());
			ps.setString(2, journal.getAuthor());
			ps.setString(3, journal.getArticle());
			ps.setDate(4, date);
			ps.setInt(5, journal.getCategory());
			ps.setInt(6, journal.getId());
			ps.executeUpdate();
		}
	}
	
	/*
	 * Method that delete a journal in journals table
	 *
	 * @param journal: specific the journal with id to delete
	 */
	@Override
	public void deleteJournal(Journal journal) throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			String sql = "DELETE FROM journals " + " WHERE journal_id = ?; ";
			// Statement s = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, journal.getId());
			ps.executeUpdate();
		}
	}


	/*
	 * Method that inserts a journal into the journals table 
	 *
	 * @param journal: the new journal to be inserted
	 */
	@Override
	public void insertJournal(Journal journal) throws SQLException {
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){

			DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	
			String createDate = dateformat.format(journal.getCreatedDate());
	
			//System.out.println("Create Date:" + createDate + " " + journal.getCategory());
			java.sql.Date date = java.sql.Date.valueOf(createDate);
			String sql = "INSERT INTO journals(journal_title, journal_author, journal_content, journal_create_date, journal_category_fk) "
					+ "VALUES (?,?,?,?,?);";
			Statement s = conn.createStatement();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, journal.getTitle());
			ps.setString(2, journal.getAuthor());
			ps.setString(3, journal.getArticle());
			ps.setDate(4, date);
			ps.setInt(5, journal.getCategory());
			ps.executeUpdate();
		}

	}

	/*
	 * Method that retrieve a list of journal from journals table
	 *
	 * @param number_of_articles: the maximum amount of journals can be retrieved
	 */
	@Override
	public ArrayList<Journal> retrieveJournals(int number_of_articles) throws SQLException {
	
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			ResultSet rs = null; // initailize an empty set that will store the results of our query
			String sql = "SELECT * FROM journals LIMIT " + number_of_articles;
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
	
			int i = 0;
			while (rs.next() && i < number_of_articles) {
				String arr = rs.getString("journal_content");
				Journal j = new Journal(rs.getInt("journal_id"), rs.getString("journal_title"), rs.getString("journal_author"),
						rs.getDate("journal_create_date"), arr, rs.getInt("journal_category_fk"));
				listOfJournals.add(j);
	
				i++;
	
			}
		}
		// System.out.println(listOfJournals.toString());
		return listOfJournals;
	}
	/*
	 * Method that retrieve a list of journal from journals table
	 *
	 */
	@Override
	public ArrayList<Journal> retrieveAllJournals() throws SQLException {
		//listOfJournals = new ArrayList<Journal>();
		try(Connection conn = PostgreSQLConnect.getInstacne().getConnection()){
			ResultSet rs = null;
			String sql = "SELECT * FROM journals";
			Statement s = conn.createStatement();
			rs = s.executeQuery(sql);
			int i = 0;
			while (rs.next()) {
				String arr = rs.getString("journal_content");
				Journal j = new Journal(rs.getInt("journal_id"), rs.getString("journal_title"), rs.getString("journal_author"),
						rs.getDate("journal_create_date"), arr, rs.getInt("journal_category_fk"));
				listOfJournals.add(j);
				i++;
	
			}
		}
		return listOfJournals;
	}

	/*
	 * return the journals by its id within the journal list
	 */
	@Override
	public Journal getJournalById(int id) throws SQLException {
		return listOfJournals.get(id);
	}

	/*
	 * reset the hashmaps
	 */
	@Override
	public void reset() {
		//listOfAuthors.clear();
		listOfJournals.clear();
		//listOfCategorys.clear();
	}

}
