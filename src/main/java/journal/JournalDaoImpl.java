package journal;

public interface JournalDaoImpl {
	Journal getJournalById(int id);
	Journal[] retrieveAllJournals();
}
