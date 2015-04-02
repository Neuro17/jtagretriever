package dataBaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import entity.Artist;
import entity.Event;
import entity.Venue;

public class EventService extends DatabaseService implements EventDAOInterface{

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
	
		String sql = "select * " +
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
			Double latitude = resultSet.getDouble("latitude");
			Double longitude = resultSet.getDouble("longitude");
			
			eTmp.setId(eventId);
			eTmp.setTitle(title);
			eTmp.setDatetime(dateTime);
			eTmp.setDescription(description);

			VenueService vService = new VenueService();
			Venue v = vService.find(latitude,longitude);
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
	  ArtistService aService = new ArtistService();
	  
	  vService.persist(entity.getVenue());
	  
	  if(!this.exists(entity.getId()) && 
			  vService.exists(entity.getVenue().getLatitude(),entity.getVenue().getLongitude())){
	    try {
	    	this.configure();
	        
		    preparedStatement = connection
		    		.prepareStatement("INSERT INTO "
		    				+ "`concerts_db`.`events_table` "
		    				+ "(`event_id`, `title`, `datetime`, `description`, "
		    				+ "`latitude`, `longitude`) "
		    				+ "VALUES (?, ?, ?, ?, ?, ?)");
		    preparedStatement.setInt(1, entity.getId());
		    preparedStatement.setString(2, entity.getTitle());
		    preparedStatement.setTimestamp(3, new Timestamp(entity.getDatetime().getMillis()));
		    preparedStatement.setString(4, entity.getDescription());
		    preparedStatement.setDouble(5, entity.getVenue().getLatitude());
		    preparedStatement.setDouble(6, entity.getVenue().getLongitude());
		    preparedStatement.executeUpdate();
		    	   
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	  
	    ArrayList<Artist> eArtist = entity.getArtist();
	    
	    for(Artist a : eArtist) {
	    	aService.persist(a);
	    	if (!aService.existsPartecipations(a.getName(), entity.getId())) {
		    	try {
			    	this.configure();
			        
			        preparedStatement = connection.prepareStatement("INSERT INTO " +
			        		"partecipations(`event_id`, `artist_name`) VALUES(?,?)");
				    preparedStatement.setInt(1, entity.getId());
				    preparedStatement.setString(2, a.getName());
				    System.out.println(preparedStatement);
				    preparedStatement .executeUpdate();
				    
			    } catch (Exception e) {
			      throw e;
			    } finally {
			      close();
			    }
	    	}
	    }
	  }
	  
  }

  public void update(Event entity) throws Exception{
	 VenueService vService = new VenueService();
	 Venue vTmp = vService.find(entity.getVenue().getLatitude(),entity.getVenue().getLongitude());
	 	 
	 if(exists(entity.getId()) && vTmp != null){
		 
		 System.out.println("updating events table");
		 try {
			this.configure();
			  		  
	      	preparedStatement = 
	      			connection.prepareStatement("UPDATE `concerts_db`.`events_table` "
	      					+ "SET `title`=?, `datetime`=?, `description`=?, "
	      					+ "`latitude`=?, `longitude`=? WHERE `event_id`=?");
		    preparedStatement.setString(1, entity.getTitle());
		    preparedStatement.setTimestamp(2, new Timestamp(entity.getDatetime().getMillis()));		
		    preparedStatement.setString(3, entity.getDescription());
		    preparedStatement.setDouble(4, entity.getVenue().getLatitude());
		    preparedStatement.setDouble(5, entity.getVenue().getLongitude());
		    preparedStatement.setInt(6, entity.getId());
		    preparedStatement .executeUpdate();
		    
		    System.out.println(preparedStatement);
		    
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }

		 System.out.println("updated events table");


		 System.out.println("deleting from partecipations table");
	 try {
		this.configure();

		preparedStatement = connection.prepareStatement("delete from partecipations where event_id = ?");
		preparedStatement.setInt(1,entity.getId());
		preparedStatement.execute(); 	    
    
		System.out.println(preparedStatement);
		
	} catch (Exception e) {
	  throw e;
	} finally {
	  close();
	}
	 System.out.println("deleted from partecipations table");
	 
	 System.out.println("adding to partecipations table");
    ArrayList<Artist> eArtist = entity.getArtist();
    for(Artist a : eArtist)
    	try {
	    	this.configure();
	        
        preparedStatement = connection.prepareStatement("INSERT INTO " +
        		"partecipations(`event_id`, `artist_name`) VALUES(?,?)");
	    preparedStatement.setInt(1, entity.getId());
	    preparedStatement.setString(2, a.getName());
	    preparedStatement .executeUpdate();
	      
	    System.out.println(preparedStatement);
	    
    } catch (Exception e) {
      throw e;
    } finally {
      close();
    }
	 System.out.println("added to partecipations table");
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

		partialEvents = extractEvents(resultSetTmp);
	
		finalEvents = extractPartecipations(partialEvents);

    return finalEvents;
    
  }

  public void deleteAll() throws Exception {
	ArrayList<Event> entities = this.findAll();
	for(Event entity : entities){
		this.delete(entity.getId());	      
	}
}

  private ArrayList<Event> extractEvents(ResultSet resultSetTmp) throws SQLException, Exception{
	  ArrayList<Event> partialEvents = new ArrayList<Event>();

	  while (resultSetTmp.next()) {
	    	Integer id = resultSetTmp.getInt("event_id");
	        String title = resultSetTmp.getString("title");
	    	Timestamp ts = (resultSetTmp.getTimestamp("datetime"));    	
	    	DateTime dateTime = new DateTime((long)ts.getTime());
	        String description = resultSetTmp.getString("description");
	        Double latitude = resultSetTmp.getDouble("latitude");
	        Double longitude = resultSetTmp.getDouble("longitude");
	        Event e = new Event();
	        e.setId(id);
	        e.setTitle(title);
	        e.setDatetime(dateTime);
	        e.setDescription(description);

	        VenueService vService = new VenueService();
	        Venue v = vService.find(latitude,longitude);
	        e.setVenue(v);

	        partialEvents.add(e);
	    }
	return partialEvents;	
  }

  private ArrayList<Event> extractPartecipations(ArrayList<Event> partialEvents) throws Exception{
		
//	    try {
			  this.configure();

			  statement = connection.createStatement();
		      resultSet = statement.executeQuery("select * "
		      		+ "from concerts_db.partecipations");
//	    } catch (Exception e) {
//	      throw e;
//	    } finally {
//	      close();
//	    }
		
	    ArrayList<Event> finalEvents = new ArrayList<Event>();
		for(int i = 0 ; i < partialEvents.size(); i++){
			resultSet.absolute(0);
			ArrayList<Artist> eArtist = new ArrayList<Artist>();
		  		
			Event eTmp = partialEvents.get(i);
			
			while (resultSet.next()) {
				Integer eventId = resultSet.getInt("event_id");
				if(eventId == eTmp.getId()){
		        	String artistName = resultSet.getString("artist_name");
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
  
  public ArrayList<Event> getTodaysEvents(LocalDate today) throws Exception {
		ResultSet resultSetTmp = null;
//		try {
			  this.configure();
		      preparedStatement = 
		    		  connection.prepareStatement("select * "
		    		  		+ "from `concerts_db`.`events_table`"
		    		  		+ "where date(`datetime`) like ?");   
		      preparedStatement.setString(1, today.toString());		

		      resultSetTmp = preparedStatement.executeQuery();   
		      
//		    } catch (Exception e) {
//		      throw e;
//		    } finally {
//		      close();
//		    }	  
		      
		ArrayList<Event> partialEvents = extractEvents(resultSetTmp);

		ArrayList<Event> finalEvents = extractPartecipations(partialEvents);
	    
		return finalEvents;
	}

} 