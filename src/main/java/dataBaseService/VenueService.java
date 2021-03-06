package dataBaseService;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javabandsintown.search.Bandsintown;
import javabandsintown.entity.Venue;

public class VenueService extends DatabaseService implements VenueDAOInterface {

	private static final Logger log = LogManager.getLogger(VenueService.class);
	
	  private void updateResearches(String venueName) throws Exception {
			 try {
				  configure();
			      
			      String updateTableSQL = "UPDATE venues_searched "
			      		+ "SET total = total + 1 WHERE venue_name = ?";
			      preparedStatement = connection.prepareStatement(updateTableSQL);
			      preparedStatement.setString(1, venueName);
			      System.out.println(preparedStatement);
			      preparedStatement .executeUpdate();
			      
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }

		}

	public boolean exists(double lat,double lng) throws Exception{
	  try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("select * from venues "
	      		+ "where latitude = ? and longitude = ?");   
	      preparedStatement.setDouble(1,lat);
	      preparedStatement.setDouble(2,lng);
          System.out.println(preparedStatement);

          resultSet = preparedStatement.executeQuery();   
          
          if(!resultSet.next()){
        	  return false;  
          }
          else {
        	  return true;  
          }
          
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
	}
	
	  public boolean checkName(String venueName) throws Exception{
		  try {
			  configure();
		      
		      preparedStatement = connection.prepareStatement("select * "
	      		+ "from venues where `venue_name` like '%" + venueName + "%'");   

		      System.out.println(preparedStatement);
	          
		      resultSet = preparedStatement.executeQuery();   
	          
	          if(resultSet.last()){
	        	  String venueNameTmp = resultSet.getString("venue_name");
	        	  updateResearches(venueNameTmp);
	        	  return true;
	          }
	          else {
	        	  return false;
	          }
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }	  
	  }

public Venue find(double lat, double lng) throws Exception{
	  Venue venueTmp = null;
	  try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("select * "
	      		+ "from venues where latitude = ? and longitude = ?");   
	      preparedStatement.setDouble(1,lat);
	      preparedStatement.setDouble(2,lng);
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

  public ArrayList<Venue> findByName(String venueName) throws Exception{
	  Venue venueTmp = null;
	  ArrayList<Venue> venues = new ArrayList<Venue>();
	  try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("select * "
	      		+ "from venues where `venue_name` like '%" + venueName + "%'");   
	      System.out.println(preparedStatement);
          resultSet = preparedStatement.executeQuery();   

          while(resultSet.next()){
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
              
              venues.add(venueTmp);
	      }
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
	  return venues;
}  

  public void persist(Venue entity) throws Exception{
	  if(!exists(entity.getLatitude(),entity.getLongitude())){
	    try {
	    	this.configure();
	        
		    preparedStatement = connection
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
		    
		    System.out.println(preparedStatement);
		    
		    preparedStatement.executeUpdate();

	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	    persistSearch(entity);
	  }
  }

  private void persistSearch(Venue venue) throws Exception{
	  try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement(
		    		"INSERT INTO `concerts_db`.`venues_searched`(`venue_name`, `total`) "
		    		+ "VALUES (?, ?)"
		    		+ " ON DUPLICATE KEY UPDATE `total` = `total` + 1");
		    preparedStatement.setString(1, venue.getName());
		    preparedStatement.setInt(2, 1);
		    System.out.println(preparedStatement);	      
		    preparedStatement .executeUpdate();
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  }

public void update(Venue entity) throws Exception{
	 if(exists(entity.getLatitude(),entity.getLongitude()))
		 try {
			  this.configure();
		      String sql = "UPDATE `concerts_db`.`venues` SET `venue_id`=?, "
		      		+ "`venue_name`=?, `country`=?, `city`=?, `region`=?"
		      		+ " WHERE `latitude`=? and`longitude`=?";
		      preparedStatement = connection.prepareStatement(sql);
		      preparedStatement.setInt(1, entity.getId());
		      preparedStatement.setString(2, entity.getName());
		      preparedStatement.setString(3, entity.getCountry());
		      preparedStatement.setString(4, entity.getCity());
		      preparedStatement.setString(5, entity.getRegion());
		      preparedStatement.setDouble(6, entity.getLatitude());
		      preparedStatement.setDouble(7, entity.getLongitude());
		      preparedStatement .executeUpdate();
		      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  } 
  
  public void delete(double lat,double lng) throws Exception {
	 if(exists(lat,lng))
	 try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("delete from venues "
	      		+ "where latitude = ? and longitude = ?");
	      preparedStatement.setDouble(1,lat);
	      preparedStatement.setDouble(2,lng);
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
	  
      statement = connection.createStatement();
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
		this.delete(entity.getLatitude(),entity.getLongitude());
  }

public boolean manageTag(String tag) throws Exception {
	Bandsintown bandsintown = new Bandsintown();
	
	ArrayList<Venue> venues = bandsintown.getVenues.query(tag).asJson().search();

	if(!venues.isEmpty()){
		for(Venue vTmp : venues)
			persist(vTmp);
		persistSearches(tag);
		return true;
	}
	else
		return false;
}

private void persistSearches(String tag) throws Exception {	
	  try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement(
		    		"INSERT INTO venues_searched(venue_name,total) VALUES(?,?)"
		    		+ "ON DUPLICATE KEY UPDATE `total` = `total` + 1");
		    preparedStatement.setString(1, tag);
		    preparedStatement.setInt(2, 1);
		    System.out.println(preparedStatement);	      
		    preparedStatement .executeUpdate();
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
}

public ArrayList<String> top(int i) throws Exception {
		ArrayList<String> venuesNames = new ArrayList<String>();

		try {
			  configure();

		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from `concerts_db`.`venues_searched` "
		      		+ "ORDER BY `total` DESC LIMIT ?");   
		      preparedStatement.setInt(1,i);
		      System.out.println(preparedStatement);
		      resultSet = preparedStatement.executeQuery();   
				      
		      while (resultSet.next()) {
		          String name = resultSet.getString("venue_name");
		          venuesNames.add(name);
		      }		
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	  	
		return venuesNames;
	}
} 