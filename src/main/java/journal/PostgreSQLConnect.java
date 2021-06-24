/**
 * 
 */
package journal;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.sql.Connection;

/**
 * Network manager that connects to a postgre database and interact with the database 
 * 
 * @author Kenneth Eng
 *
 */
public class PostgreSQLConnect {
	private static PostgreSQLConnect postgreCon;
	Properties prop = getProperties();
	
	public static Logger log = LogManager.getLogger( PostgreSQLConnect.class);
	private PostgreSQLConnect() {
		
	}
	
	private Properties getProperties() {
		Properties prop = new Properties();
		try {
			String path = "C:/Users/ocean/Documents/revature/ben_section/proj1/P0-Kenneth_Eng/src/main/resourses/config.properties";
			FileInputStream ip= new FileInputStream(path);
			prop.load(ip);
			
			//System.out.println(prop.getProperty("url") + prop.getProperty("db") +prop.getProperty("pw"));
			
			return prop;
		}catch(Exception e) {
			log.error("In PostgreSQLConnect: fail to load database configure file");
		}
		return prop;
	}
	
	public static PostgreSQLConnect getInstacne() {
		return postgreCon = new PostgreSQLConnect();
	}
	
	public  Connection getConnection() {
		Connection c = null;
		try {
			Class.forName("org.postgresql.Driver");
			c = DriverManager.getConnection(prop.getProperty("url"), prop.getProperty("db"), prop.getProperty("pw"));
			//System.out.println("Opened database successfully");
		} catch (Exception e) {
			log.error("In PostgreSQLConnect: Fail to connect the database with the given properties");
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		
		return c;
		
	}
	
	
}
