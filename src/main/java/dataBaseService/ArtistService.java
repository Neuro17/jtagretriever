package dataBaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import app.models.interfaces.ArtistDAOInterface;
import entity.Artist;

@PropertySource("classpath:config.properties")
public class ArtistService implements ArtistDAOInterface {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

	  private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	  private static final String DB_URL = "jdbc:mysql://localhost/concerts_db";
	  private static final String USER = "root";
	  private static final String PASS = "mysqldata";
  
//  @Value("${spring.datasource.driverClassName}")
//  private String JDBC_DRIVER;
//  
//  @Value("${spring.datasource.url}")
//  private static final String DB_URL;
  
  private void configure() throws ClassNotFoundException, SQLException{
	  Class.forName(getJdbcDriver());
      connect = DriverManager.getConnection(getDbUrl() + "?user=" + getUser() + "&password=" + getPass());
  }
  
  public boolean exists(String id) throws Exception{
	  try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("select * from artists where artist_name = ?");   
	      preparedStatement.setString(1,id);
          resultSet = preparedStatement.executeQuery();   
          
          if(!resultSet.next())
        	  return false;
          else return true;
          
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
  }
  
  public boolean existsPartecipations(String id, int event_id) throws Exception{
	  try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("select * from partecipations where artist_name = ? and event_id = ?");   
	      preparedStatement.setString(1,id);
	      preparedStatement.setInt(2,event_id);
	      System.out.println(preparedStatement);
          resultSet = preparedStatement.executeQuery();   
          System.out.println(resultSet);
          if(!resultSet.next())
        	  return false;
          else return true;
          
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
  }
  
  public Artist findById(String id) throws Exception{
	  Artist aTmp = null;
	  try {
			  this.configure();
		      
		      preparedStatement = connect.prepareStatement("select * from artists where artist_name = ?");   
      preparedStatement.setString(1,id);
      resultSet = preparedStatement.executeQuery();   
      
      if(resultSet.last()){
          String nameTmp = resultSet.getString("artist_name");
          String idTmp = resultSet.getString("artist_id");
          aTmp = new Artist(nameTmp,idTmp);
      }
      else 
    	  System.out.println("Artist with Name : " + id + " not found");
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
	  return aTmp;
  }

  public void persist(Artist entity) throws Exception{
	 if(!exists(entity.getName()))
	    try {
	    	this.configure();
	        
		    preparedStatement = connect.prepareStatement("INSERT INTO artists(artist_id,artist_name) VALUES(?,?)");
		    preparedStatement.setString(1, entity.getId());
		    preparedStatement.setString(2, entity.getName());
		    preparedStatement .executeUpdate();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  }

  public void update(Artist entity) throws Exception{
	 if(exists(entity.getName()))
		 try {
			  this.configure();
		      
		      String updateTableSQL = "UPDATE artists SET artist_id = ? WHERE artist_name = ?";
		      preparedStatement = connect.prepareStatement(updateTableSQL);
		      preparedStatement.setString(1, entity.getId());
		      preparedStatement.setString(2, entity.getName());
		      preparedStatement .executeUpdate();
		      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  } 
  
  public void delete(String id) throws Exception {
	 if(exists(id))
	 try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("delete from artists where artist_name = ?");
	      preparedStatement.setString(1,id);
	      preparedStatement.execute();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
}
  
  public ArrayList<Artist> findAll() throws Exception {
	ArrayList<Artist> entities = new ArrayList<Artist>();

	try {
		  this.configure();
	      statement = connect.createStatement();
	
	      resultSet = statement.executeQuery("select * from artists");
	      while (resultSet.next()) {
	          String name = resultSet.getString("artist_name");
	          String id = resultSet.getString("artist_id");
	          Artist a = new Artist(name,id);
	          entities.add(a);
	      }
	
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
  	return entities;
  }

	public void deleteAll() throws Exception {
		ArrayList<Artist> entities = this.findAll();
		for(Artist entity : entities)
			this.delete(entity.getName());
	}
  
	public ArrayList<Artist> getEventArtist(int eventId) throws Exception{
		ArrayList<Artist> eventArtist = null;
		ArrayList<String> artistsNames = null;
		ResultSet resultSetTmp = null;
		
		try {
			  this.configure();
		      
		      preparedStatement = connect.prepareStatement("select * from partecipations where event_id =?");   
		      preparedStatement.setInt(1,eventId);
	          resultSetTmp = preparedStatement.executeQuery();   
	          
	          artistsNames = new ArrayList<String>();
	          while (resultSetTmp.next()) {
	              String name = resultSetTmp.getString("artist_name");
	              artistsNames.add(name);
	          }
	          
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }
	
		if(artistsNames.size() > 0){
		eventArtist =new ArrayList<Artist>();
			for(String name : artistsNames){
		        Artist a = this.findById(name);
		        eventArtist.add(a);
			}
		}
	
		return eventArtist;
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