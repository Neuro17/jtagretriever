package twitter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import twitter4j.Status;

public class JDBC {
	protected Statement statement;
	protected Connection connection;

	public JDBC(String db) throws ClassNotFoundException, SQLException {
		if (db.equals("sqlite")) {
			// load the sqlite-JDBC driver using the current class loader
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
		} else if (db.equals("postgres")) {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql:///dbname", "username", "password");
		} else {
			throw new ClassNotFoundException("database not found");
		}
		statement = connection.createStatement();
		statement.setQueryTimeout(30);  
	}

	public void getColumnNames() throws SQLException {
		StringBuilder sb = new StringBuilder();
		System.out.println("- reading database (column names)...");
		ResultSet rs = statement.executeQuery("SELECT * FROM person LIMIT 1 OFFSET 0");

		while (rs.next()){
			for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) { 
				sb.append(rs.getMetaData().getColumnName(i));
				sb.append(",");
			}
		}
		System.out.println(sb.toString());
	}

	public void testInsert(ArrayList<Status> tweets) throws SQLException {
		System.out.println("- building database...");
		System.out.println(tweets.get(0).getId());
//		statement.executeUpdate("DROP TABLE IF EXISTS tweets");
//		statement.executeUpdate("CREATE TABLE tweets (id LONG INTEGER PRIMARY KEY, text VARCHAR(140), lat FLOAT, lng FLOAT, place VARCHAR(50), ora VARCHAR(20))");
		StringBuilder sql = new StringBuilder();
		try {
			for(Status t : tweets){
				statement.executeUpdate("INSERT INTO tweets VALUES('"+t.getId()+"','"+t.getText().replaceAll("'", " ")+"', '"+t.getGeoLocation().getLatitude()+"', '"+t.getGeoLocation().getLongitude()+"', '"+t.getPlace().getName()+"', '"+t.getCreatedAt()+"')");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void resetDB() throws SQLException {
		System.out.println("- building database...");
//		statement.executeUpdate("DROP TABLE IF EXISTS tweets");
		statement.executeUpdate("CREATE TABLE tweets (id BIGINT PRIMARY KEY, text VARCHAR(140), lat FLOAT, lng FLOAT, place VARCHAR(50), ora VARCHAR(20))");
	}

	public void testUpdate() throws SQLException {
		System.out.println("- updating database...");
		statement.executeUpdate("UPDATE person SET name='pippo' WHERE name='leo'");
	}

	public ResultSet testSelect() throws SQLException {
		System.out.println("- reading database...");
		ResultSet rs = statement.executeQuery("SELECT * FROM tweets");
//		while(rs.next()) {
//			// read the result set
//			System.out.println(rs.getRow() + ": id=" + rs.getInt("id") + ", name=" + rs.getString("name") + ", surname=" + rs.getString("surname"));
//		}
		return rs;
	}

	public void closeConnection() {
		if (connection != null)
			try {
				// relevant after 
				// connection.setAutoCommit(false);
				// connection.commit();
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void main(String[] args) throws ClassNotFoundException {
		JDBC s = null;

		try{
			s = new JDBC("sqlite");
			s.resetDB();
//			s.testInsert();
//			s.getColumnNames();
//			s.testSelect();
//			s.testUpdate();
//			s.testSelect();
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (s != null) s.closeConnection();
		}
	}
}
