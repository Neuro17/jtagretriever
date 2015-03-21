package dataBaseService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;

import entity.Artist;
import entity.Event;
import entity.Venue;

public class EventService {
  private Connection connection = null;
  private Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
  static final String DB_URL = "jdbc:mysql://localhost/concerts_db";
  static final String USER = "root";
  static final String PASS = "mysql";
  
  public void configure() throws ClassNotFoundException, SQLException{
	  Class.forName(getJdbcDriver());
	  connection = DriverManager.getConnection(getDbUrl() + "?user=" + getUser() + "&password=" + getPass());
  }

  public boolean exists(Integer id) throws Exception{
	  try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("select * from events_table where event_id =?");   
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
  
  public Event findById(Integer id) throws Exception{
	  Event eTmp = new Event();
	  ArrayList<Artist> eArtist = null;
	  
	try {
		this.configure();
	
		String sql = "select `event_id`,`title`,`datetime`,`description`,`venue_id` " +
		  		"from `concerts_db`.`events_table` where (`event_id` = ?)";
		
		preparedStatement = connection.prepareStatement(sql);   
		preparedStatement.setInt(1,id);
		resultSet = preparedStatement.executeQuery();

		if(resultSet.last()){
		      
			Integer eventId = resultSet.getInt("event_id");
			String title = resultSet.getString("title");
			Timestamp ts = (resultSet.getTimestamp("datetime"));    	
			DateTime dateTime = new DateTime((long)ts.getTime());
			String description = resultSet.getString("description");
			Integer venueId = resultSet.getInt("venue_id");
			
			eTmp.setId(eventId);
			eTmp.setTitle(title);
			eTmp.setDatetime(dateTime);
			eTmp.setDescription(description);

			VenueService vService = new VenueService();
			Venue v = vService.findById(venueId);
			eTmp.setVenue(v);
		}

  } catch (Exception ex) {
    throw ex;
  } finally {
    close();
  }

	ArtistService aService = new ArtistService();
	eArtist = aService.getEventArtist(eTmp.getId());
	eTmp.setArtist(eArtist);             


	return eTmp;
  }

  public void persist(Event entity) throws Exception{
	  VenueService vService = new VenueService();
	  
	  if(!this.exists(entity.getId()) && vService.exists(entity.getVenue().getId())){
	    try {
	    	this.configure();
	        
		    preparedStatement = connection
		    		.prepareStatement("INSERT INTO " +
		    				"events_table(`event_id`, `title`, `datetime`, `description`, `venue_id`)" +
		    				" VALUES(?,?,?,?,?)");
		    preparedStatement.setInt(1, entity.getId());
		    preparedStatement.setString(2, entity.getTitle());
		    preparedStatement.setTimestamp(3, new Timestamp(entity.getDatetime().getMillis()));
		    preparedStatement.setString(4, entity.getDescription());
		    preparedStatement.setInt(5, entity.getVenue().getId());
		    preparedStatement.executeUpdate();
		    	   
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	  
	    ArrayList<Artist> eArtist = entity.getArtist();
	    for(Artist a : eArtist)
	    	try {
		    	this.configure();
		        
		        preparedStatement = connection.prepareStatement("INSERT INTO " +
		        		"partecipations(`event_id`, `artist_name`) VALUES(?,?)");
			    preparedStatement.setInt(1, entity.getId());
			    preparedStatement.setString(2, a.getName());
			    preparedStatement .executeUpdate();
		      
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }
	  }
	  
  }

  public void update(Event entity) throws Exception{
	 VenueService vService = new VenueService();
	 Venue vTmp = vService.findById(entity.getVenue().getId());

	 if(exists(entity.getId()) && vTmp != null){
	 try {
		this.configure();
		  		  
      	preparedStatement = connection.prepareStatement("UPDATE `concerts_db`.`events_table` SET `title`=?, `datetime`=?, " +
      			"`description`=?, `venue_id`=? WHERE `event_id`=?");
	    preparedStatement.setString(1, entity.getTitle());
	    preparedStatement.setTimestamp(2, new Timestamp(entity.getDatetime().getMillis()));		
	    preparedStatement.setString(3, entity.getDescription());
	    preparedStatement.setInt(4, entity.getVenue().getId());
	    preparedStatement.setInt(5, entity.getId());
	    preparedStatement .executeUpdate();
	    
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }

	 try {
		this.configure();

		preparedStatement = connection.prepareStatement("delete from partecipations where event_id = ?");
		preparedStatement.setInt(1,entity.getId());
		preparedStatement.execute(); 	    
    
	} catch (Exception e) {
	  throw e;
	} finally {
	  close();
	}
	 
    ArrayList<Artist> eArtist = entity.getArtist();
    for(Artist a : eArtist)
    	try {
	    	this.configure();
	        
        preparedStatement = connection.prepareStatement("INSERT INTO " +
        		"partecipations(`event_id`, `artist_name`) VALUES(?,?)");
	    preparedStatement.setInt(1, entity.getId());
	    preparedStatement.setString(2, a.getName());
	    preparedStatement .executeUpdate();
	      
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
 }

} 
    
  public void delete(Integer id) throws Exception {
	 if(exists(id)){
	 try {
		  this.configure();
	      
	      preparedStatement = connection.prepareStatement("delete from events_table where event_id = ?");
	      preparedStatement.setInt(1,id);
	      preparedStatement.execute();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	 }	 
  }

  public ArrayList<Event> findAll() throws Exception {

	  ArrayList<Event> partialEvents = new ArrayList<Event>();
	  ArrayList<Event> finalEvents = new ArrayList<Event>();
	  ResultSet resultSetTmp = null;
	
//	try {
	  	this.configure();

		statement = connection.createStatement();
		resultSetTmp = statement.executeQuery("select * from `concerts_db`.`events_table`");
	
//	} catch (Exception e) {
//      throw e;
//    } finally {
//      close();
//    }

	while (resultSetTmp.next()) {
    	Integer id = resultSetTmp.getInt("event_id");
        String title = resultSetTmp.getString("title");
    	Timestamp ts = (resultSetTmp.getTimestamp("datetime"));    	
    	DateTime dateTime = new DateTime((long)ts.getTime());
        String description = resultSetTmp.getString("description");
        Integer venueId = resultSetTmp.getInt("venue_id");
        Event e = new Event();
        e.setId(id);
        e.setTitle(title);
        e.setDatetime(dateTime);
        e.setDescription(description);

        VenueService vService = new VenueService();
        Venue v = vService.findById(venueId);
        e.setVenue(v);

        partialEvents.add(e);
    }
	
//    try {
		  this.configure();

		  statement = connection.createStatement();
	      resultSetTmp = statement.executeQuery("select * from concerts_db.partecipations");
//    } catch (Exception e) {
//      throw e;
//    } finally {
//      close();
//    }
	
    for(int i = 0 ; i < partialEvents.size(); i++){
		resultSetTmp.absolute(0);
		ArrayList<Artist> eArtist = new ArrayList<Artist>();
	  		
		Event eTmp = partialEvents.get(i);
		
		while (resultSetTmp.next()) {
			Integer eventId = resultSetTmp.getInt("event_id");
			if(eventId == eTmp.getId()){
	        	String artistName = resultSetTmp.getString("artist_name");
	        	ArtistService aService = new ArtistService();
	        	Artist aTmp = aService.findById(artistName);
	        	if(aTmp != null)
	        		eArtist.add(aTmp);
	        }			
	    }
		eTmp.setArtist(eArtist);
		finalEvents.add(eTmp);
	}	
    return finalEvents;
  }

  public void deleteAll() throws Exception {
	ArrayList<Event> entities = this.findAll();
	for(Event entity : entities){
		this.delete(entity.getId());	      
	}
}
  
  private void close() {
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