package dataBaseService;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.mapping.Map;
import org.joda.time.DateTime;
import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.deMorganRewriter;
import org.springframework.context.annotation.PropertySource;

import app.controllers.JSPController;
import javabandsintown.search.Bandsintown;
import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;
import javabandsintown.entity.Venue;

@PropertySource("classpath:config.properties")
public class ArtistService extends DatabaseService implements ArtistDAOInterface{
  
	private static final Logger log = LogManager.getLogger(ArtistService.class);
//	
	private static String complete(int i){
		if(i < 10)
			return  "0" + Integer.toString(i);
		else
			return
					Integer.toString(i);
	}
	
  private void updateResearches(String artistName) throws Exception{
		 try {
			  configure();
		      
		      String updateTableSQL = "UPDATE artists_searched "
		      		+ "SET total = total + 1 WHERE artist_name = ?";
		      preparedStatement = connection.prepareStatement(updateTableSQL);
		      preparedStatement.setString(1, artistName);
System.out.println(preparedStatement);
		      preparedStatement .executeUpdate();
		      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  }
	
  public boolean checkName(String id) throws Exception{
//log.trace("entering checkname");
	  try {
		  configure();
	      
	      preparedStatement = connection.prepareStatement("select * "
	      		+ "from artists where artist_name = ?");   
	      preparedStatement.setString(1,id);
System.out.println(preparedStatement);
          resultSet = preparedStatement.executeQuery();   
          
          if(!resultSet.next()){
        	  return false;
          }
          else {
        	  updateResearches(id);
        	  return true;
          }
          
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }	  
  }
  
  public boolean existsPartecipations(String id, int event_id) throws Exception{
	  try {
		  configure();
	      
	      preparedStatement = connection.prepareStatement("select * "
	      		+ "from partecipations where artist_name = ? and event_id = ?");   
	      preparedStatement.setString(1,id);
	      preparedStatement.setInt(2,event_id);

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
  
  public Artist findById(String id) throws Exception{
	  Artist aTmp = null;
	  try {
			  configure();
		      
		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from artists where artist_name = ?");   
      preparedStatement.setString(1,id);
      resultSet = preparedStatement.executeQuery();   
      
      if(resultSet.last()){
          String nameTmp = resultSet.getString("artist_name");
          String idTmp = resultSet.getString("artist_id");
          String urlImage = resultSet.getString("url_image");
          
          aTmp = new Artist(nameTmp,idTmp);
          aTmp.setUrlImage(urlImage);
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
  
  private void persistSearch(Artist entity) throws Exception{
	  try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement(
		    		"INSERT INTO `concerts_db`.`artists_searched`"
		    		+ "(`artist_name`,`total`) "
		    		+ "VALUES (?, ?)"
		    		+ "ON DUPLICATE KEY UPDATE `total` = `total` + 1");
		    preparedStatement.setString(1, entity.getName());
		    preparedStatement.setInt(2, 1);
System.out.println(preparedStatement);	      
		    preparedStatement .executeUpdate();
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  }

  public void persist(Artist entity) throws Exception{
	 if(!checkName(entity.getName())){
//salva l artista in concerts_db.artists
	    try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement("INSERT "
		    		+ "INTO artists(artist_id,artist_name,url_image) "
		    		+ "VALUES(?,?,?)");
		    preparedStatement.setString(1, entity.getId());
		    preparedStatement.setString(2, entity.getName());
		    preparedStatement.setString(3, entity.getUrlImage());
System.out.println(preparedStatement);	      
		    preparedStatement .executeUpdate();	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
//salva la ricerca dell artista in concerts_db.artists_researched
	    persistSearch(entity);
	 }
  }

  public void update(Artist entity) throws Exception{
	 if(checkName(entity.getName()))
		 try {
			  configure();
		      
		      String updateTableSQL = "UPDATE artists "
		      		+ "SET artist_id = ? AND url_image = ? WHERE artist_name = ?";
		      preparedStatement = connection.prepareStatement(updateTableSQL);
		      preparedStatement.setString(1, entity.getId());
		      preparedStatement.setString(2, entity.getUrlImage());
		      preparedStatement.setString(3, entity.getName());
		      preparedStatement .executeUpdate();
		      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
  } 
  
  public void delete(String id) throws Exception {
	 if(checkName(id))
	 try {
		  configure();
	      
	      preparedStatement = connection.prepareStatement("delete from artists "
	      		+ "where artist_name = ?");
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
		  configure();
	      statement = connection.createStatement();
	
	      resultSet = statement.executeQuery("select * from artists");
	      while (resultSet.next()) {
	          String name = resultSet.getString("artist_name");
	          String id = resultSet.getString("artist_id");
	          String urlImage = resultSet.getString("url_image");
	          Artist a = new Artist(name,id);
	          a.setUrlImage(urlImage);
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
			  configure();
		      
		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from partecipations where event_id =?");   
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
	
	public boolean manageTag(String tag) throws Exception{
//log.trace("entering manageTag");
		Bandsintown bandsintown = new Bandsintown();

		Artist aTmp = bandsintown.getArtist.setArtist(tag).asJson().search();

		if(aTmp != null){
			persist(aTmp);
			EventService eS = new EventService();
//log.trace("searching events for artist " + aTmp.getName());
		
			DateTime now = new DateTime();
			DateTime timeAgo = now.minusMonths(9);
			DateTime timeForward = now.plusMonths(3);

			String timeForwardString = timeForward.getYear() + "-" + 
					complete(timeForward.getMonthOfYear()) + "-" + 
					complete(timeForward.getDayOfMonth());
			String timeAgoString = timeAgo.getYear() + "-" + 
					complete(timeAgo.getMonthOfYear()) + "-" + 
					complete(timeAgo.getDayOfMonth());
			String datesString = timeAgoString + "," + timeForwardString; 

			ArrayList<Event> events = bandsintown.getEvents
					.setArtist(aTmp.getName())
					.setDate(datesString).search();
			
//log.trace("total events " + events.size());
			while(events.size() > 0){
				int fromIndex = 0;
				int toIndex = 50;
				if(events.size() < toIndex)
					toIndex = events.size(); 
				ArrayList<Event> partialEvents = 
						new ArrayList<Event>(events.subList(fromIndex, toIndex));
//log.trace("persisting " + partialEvents.size() +" events");
				for(Event e : partialEvents)
					eS.persist(e);
				events = new ArrayList<Event>(events.subList(toIndex, events.size()));
//log.trace("remaining " + events.size() +" events");
			}				
			return true;
		}
		else{
			return false;
		}
	}

	public HashMap<String,String> top(int i) throws Exception {
		HashMap<String, String> artistsMap = new HashMap<String, String>();
		ArrayList<String> artistsList = new ArrayList<String>();
		
		try {
			  configure();

		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from `concerts_db`.`artists_searched` "
		      		+ "ORDER BY `total` DESC LIMIT ?");   
		      preparedStatement.setInt(1,i);
System.out.println(preparedStatement);
		      resultSet = preparedStatement.executeQuery();   
				      
		      while (resultSet.next()) {
		    	  String name = resultSet.getString("artist_name");
		    	  artistsList.add(name);
		      }		
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
		
		ArtistService aS = new ArtistService();
		for(String name : artistsList){
			Artist aTmp = aS.findById(name);
			if(aTmp != null)
				artistsMap.put(aTmp.getName(), aTmp.getUrlImage());
		}
	  	
		return artistsMap;
	}
	
	public ArrayList<Event> getAllEvents(String artistName) throws Exception{
		ArrayList<Event> events = new ArrayList<Event>();
		if(checkName(artistName)) { 
			try {
				configure();
   		
				preparedStatement = connection.prepareStatement("SELECT e.event_id,"
						+ "e.title, e.datetime, e.latitude, e.longitude "
						 + "FROM concerts_db.partecipations as p "
						 + "right join concerts_db.events_table as e "
						 + "on p.event_id = e.event_id "
						 + "WHERE p.artist_name like ?"
						 + "ORDER BY e.datetime");
				preparedStatement.setString(1, artistName); 
				System.out.println(preparedStatement);
				resultSet = preparedStatement.executeQuery();
//				System.out.println(resultSet.next());
				 
			      
			    while (resultSet.next()) {
			    	int id = resultSet.getInt("event_id");
			    	String title = resultSet.getString("title");
			    	Timestamp ts = resultSet.getTimestamp("datetime");    	
					DateTime date = new DateTime((long)ts.getTime());
					Double lat = resultSet.getDouble("latitude");
					Double lng = resultSet.getDouble("longitude");
					Venue venueTmp = new Venue(lat, lng);
					events.add(new Event(id, title, date, venueTmp));
//			    	String name = resultSet.getString("artist_name");
//			    	artistsLieventst.add(name);
			    }	  
			} catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }
		}
		return events;
	}
} 