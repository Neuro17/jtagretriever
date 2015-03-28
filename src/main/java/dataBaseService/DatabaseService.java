package dataBaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class DatabaseService {
	  private Connection connect = null;
	  private Statement statement = null;
	  private PreparedStatement preparedStatement = null;
	  private ResultSet resultSet = null;

	  private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	  private static final String DB_URL = "jdbc:mysql://localhost/concerts_db";
	  private static final String USER = "root";
	  private static final String PASS = "mysql";
	  
	  private void configure() throws ClassNotFoundException, SQLException{
		  Class.forName(getJdbcDriver());
	      connect = DriverManager.getConnection(getDbUrl() + "?user=" + getUser() + "&password=" + getPass());
	  }

	  private void close() {
		  try {
		      if (resultSet != null) {
		        resultSet.close();
		      }
		
		      if (statement != null) {
		        statement.close();
		      }
		
		      if (connect != null) {
		        connect.close();
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
