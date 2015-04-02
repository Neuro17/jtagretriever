package dataBaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseService {
	  protected Connection connection = null;
	  protected Statement statement = null;
	  protected PreparedStatement preparedStatement = null;
	  protected ResultSet resultSet = null;

	  private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	  private static final String DB_URL = "jdbc:mysql://localhost/concerts_db";
	  private static final String USER = "root";
	  private static final String PASS = "mysql";
	  
	  protected void configure() throws ClassNotFoundException, SQLException{
		  Class.forName(getJdbcDriver());
	      connection = DriverManager.getConnection(getDbUrl() + "?user=" + getUser() + "&password=" + getPass());
	  }

	  protected void close() {
		  try {
		      if (resultSet != null) {
		        resultSet.close();
		      }
		
		      if (statement != null) {
		        statement.close();
		      }
		
		      if (connection != null) {
		        connection.close();
		      }
		  } catch (Exception e) {
		}
	  }

	  
	  public static String getJdbcDriver() {
		  return JDBC_DRIVER;
	  }

	  public static String getDbUrl() {
		return DB_URL;
	  }

	  public static String getUser() {
		return USER;
	  }

	  public static String getPass() {
		return PASS;
	  }
}