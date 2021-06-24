package journal;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface JournalDaoImpl {
	Journal getJournalById(int id) throws SQLException;
	
	ArrayList<Journal> retrieveAllJournals() throws SQLException;
	ArrayList<Journal> retrieveJournals(int number_of_articles) throws SQLException;
	HashMap<Integer, String> getAllCategory() throws SQLException;
	
	void deleteCategory(int catgory_id)  throws SQLException ;
	void updateCategory(int catgory_id) throws SQLException;
	void insertAuthor(String firstname, String lastname) throws SQLException;
	void insertJournal(Journal journal) throws SQLException;
	void deleteJournal(Journal journal) throws SQLException;
	void reset();
}
