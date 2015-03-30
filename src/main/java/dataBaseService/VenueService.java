package dataBaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import entity.Venue;

public class VenueService {
  private Connection connect = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
  static final String DB_URL = "jdbc:mysql://localhost/concerts_db";
  static final String USER = "root";
  static final String PASS = "mysqldata";
  
  public void configure() throws ClassNotFoundException, SQLException{
	  Class.forName(getJdbcDriver());
      connect = DriverManager.getConnection(getDbUrl() + "?user=" + getUser() + "&password=" + getPass());
  }

  public boolean exists(Integer id) throws Exception{
	  try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("select * from venues where venue_id = ?");   
	      preparedStatement.setInt(1,id);
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


  public Venue findById(Integer id) throws Exception{
	  Venue venueTmp = null;
	  try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("select * from venues where venue_id =?");   
	      preparedStatement.setInt(1,id);
          resultSet = preparedStatement.executeQuery();   

          if(resultSet.last()){
        	  Integer idTmp = resultSet.getInt("venue_id");
              Double latitude = resultSet.getDouble("latitude");
              Double longitude = resultSet.getDouble("longitude");
              String name = resultSet.getString("venue_name");
              String country = resultSet.getString("country");
              String city = resultSet.getString("city");
              String region = resultSet.getString("region");
              venueTmp = new Venue();
              venueTmp.setId(idTmp);
              venueTmp.setLatitude(latitude);
              venueTmp.setLongitude(longitude);
              venueTmp.setName(name);
              venueTmp.setCountry(country);
              venueTmp.setCity(city);
              venueTmp.setRegion(region);
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
	  return venueTmp;
}

  public void persist(Venue entity) throws Exception{
	  if(!exists(entity.getId()))
	    try {
	    	this.configure();
	        
		    preparedStatement = connect
		    		.prepareStatement("INSERT INTO " +
		    				"venues(venue_id,latitude,longitude,venue_name,country,city,region)" +
		    				" VALUES(?,?,?,?,?,?,?)");
		    preparedStatement.setInt(1, entity.getId());
		    preparedStatement.setDouble(2, entity.getLatitude());
		    preparedStatement.setDouble(3, entity.getLongitude());
		    preparedStatement.setString(4, entity.getName());
		    preparedStatement.setString(5, entity.getCountry());
		    preparedStatement.setString(6, entity.getCity());
		    preparedStatement.setString(7, entity.getRegion());
		    preparedStatement .executeUpdate();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  }

  public void update(Venue entity) throws Exception{
	 if(exists(entity.getId()))
		 try {
			  this.configure();
		      String sql = "UPDATE `concerts_db`.`venues` SET `latitude`=?, `longitude`=?, `venue_name`=?," +
		      		" `country`=?, `city`=?, `region`=? WHERE `venue_id`=?";

		      preparedStatement = connect.prepareStatement(sql);
		      preparedStatement.setDouble(1, entity.getLatitude());
		      preparedStatement.setDouble(2, entity.getLongitude());
		      preparedStatement.setString(3, entity.getName());
		      preparedStatement.setString(4, entity.getCountry());
		      preparedStatement.setString(5, entity.getCity());
		      preparedStatement.setString(6, entity.getRegion());
		      preparedStatement.setInt(7, entity.getId());
		      preparedStatement .executeUpdate();
		      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  } 
  
  public void delete(Integer id) throws Exception {
	 if(exists(id))
	 try {
		  this.configure();
	      
	      preparedStatement = connect.prepareStatement("delete from venues where venue_id = ?");
	      preparedStatement.setInt(1,id);
	      preparedStatement.execute();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
}
  
  public ArrayList<Venue> findAll() throws Exception {
	ArrayList<Venue> entities = new ArrayList<Venue>();

	try {
	  this.configure();
	  
      statement = connect.createStatement();
      resultSet = statement.executeQuery("select * from concerts_db.venues");

      while (resultSet.next()) {
          Integer id = resultSet.getInt("venue_id");
          Double latitude = resultSet.getDouble("latitude");
          Double longitude = resultSet.getDouble("longitude");
          String name = resultSet.getString("venue_name");
          String country = resultSet.getString("country");
          String city = resultSet.getString("city");
          String region = resultSet.getString("region");
          Venue v = new Venue();
          v.setId(id);
          v.setLatitude(latitude);
          v.setLongitude(longitude);
          v.setName(name);
          v.setCountry(country);
          v.setCity(city);
          v.setRegion(region);
          entities.add(v);
      }

    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
	
  	return entities;
  }

  public void deleteAll() throws Exception {
	ArrayList<Venue> entities = this.findAll();
	for(Venue entity : entities)
		this.delete(entity.getId());
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