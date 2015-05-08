package dataBaseService;

import java.sql.ResultSet;
import java.util.ArrayList;

import org.springframework.context.annotation.PropertySource;

import javabandsintown.search.Bandsintown;
import javabandsintown.entity.Artist;

@PropertySource("classpath:config.properties")
public class ArtistService extends DatabaseService implements ArtistDAOInterface{
    	
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
	      
	      preparedStatement = connection.prepareStatement("select * from partecipations where artist_name = ? and event_id = ?");   
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
		      
		      preparedStatement = connection.prepareStatement("select * from artists where artist_name = ?");   
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
  
  private void persistSearch(Artist entity) throws Exception{
	  try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement(
		    		"INSERT INTO `concerts_db`.`artists_searched`(`artist_name`, `total`) "
		    		+ "VALUES (?, ?)");
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
		    		+ "INTO artists(artist_id,artist_name) VALUES(?,?)");
		    preparedStatement.setString(1, entity.getId());
		    preparedStatement.setString(2, entity.getName());
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
		      
		      String updateTableSQL = "UPDATE artists SET artist_id = ? WHERE artist_name = ?";
		      preparedStatement = connection.prepareStatement(updateTableSQL);
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
	 if(checkName(id))
	 try {
		  configure();
	      
	      preparedStatement = connection.prepareStatement("delete from artists where artist_name = ?");
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
		Bandsintown bandsintown = new Bandsintown();

		Artist aTmp = bandsintown.getArtist.setArtist(tag).asJson().search();

		if(aTmp != null){
			persist(aTmp);
			return true;
		}
		else{
			return false;
		}
	}

	public ArrayList<String> top(int i) throws Exception {
		ArrayList<String> artistsNames = new ArrayList<String>();

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
		          artistsNames.add(name);
		      }		
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	  	
		return artistsNames;
	}
} 