/**
 * 
 */
package journal;

import java.util.Arrays;
import java.util.Date;

/**
 * Journal Object contains title , author , createdDate, article 
 * @author Kenneth Eng
 *
 */
public class Journal {
	private String title;
	private String author;
	private Date createdDate;
	private String[] article;
	
	Journal(){
		
	}
	public Journal(String title, String author, Date createdDate, String[] article) {
		super();
		this.title = title;
		this.author = author;
		this.createdDate = createdDate;
		this.article = article;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String[] getArticle() {
		return article;
	}
	public void setArticle(String[] article) {
		this.article = article;
	}
	@Override
	public String toString() {
		return "Journal [title=" + title + ", author=" + author + ", createdDate=" + createdDate + ", article="
				+ Arrays.toString(article) + "]";
	}
	
	
	
	
}
