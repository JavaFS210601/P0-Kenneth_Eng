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
	private int id;
	private String title;
	private String author;
	private Date createdDate;
	private String article;
	private int category;
	
	Journal(){
		
	}
	
	Journal(String title, String author, Date createdDate, String article, int category) {

		this.title = title;
		this.author = author;
		this.createdDate = createdDate;
		this.article = article;
		this.category = category;
	}
	Journal(int id,String title, String author, Date createdDate, String article, int category) {
		this.id = id;
		this.title = title;
		this.author = author;
		this.createdDate = createdDate;
		this.article = article;
		this.category = category;
	}
	
	public int getId() {
		return id;
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
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(int category) {
		this.category = category;
	}
	
	@Override
	public String toString() {
		return "Journal [title=" + title + ", author=" + author + ", createdDate=" + createdDate + ", article="
				+ article + "]";
	}
	
	
	
	
}
