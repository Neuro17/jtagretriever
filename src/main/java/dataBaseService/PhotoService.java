package dataBaseService;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:config.properties")
public class PhotoService extends DatabaseService implements PhotoDAOInterface{
  
	private static final Logger log = LogManager.getLogger(PhotoService.class);
	
	public boolean existsAlmostsOne(int eventId) throws Exception{
		try {
			  configure();
		      
		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from photos where event_id = ?");   
		      preparedStatement.setInt(1,eventId);
		      System.out.println(preparedStatement);
	          resultSet = preparedStatement.executeQuery();   
	          
	          if(!resultSet.next())
	        	  return false;
	          else 
	        	  return true;
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }
	}

	  public boolean check(int eventId, String mediaId) throws Exception{
		  try {
			  configure();
		      
		      preparedStatement = connection.prepareStatement("select * "
		      		+ "from photos where media_id = ? and event_id = ?");   
		      preparedStatement.setString(1,mediaId);
		      preparedStatement.setInt(2,eventId);
		      System.out.println(preparedStatement);
	          resultSet = preparedStatement.executeQuery();   
	          
	          if(!resultSet.next())
	        	  return false;
	          else 
	        	  return true;
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }	  

		}
	    
  public void persist(Photo photo) throws Exception{
	 if(!check(photo.getEventId(),photo.getMediaId())){
	    try {
	    	configure();
	        
		    preparedStatement = connection.prepareStatement("INSERT "
		    		+ "INTO photos(event_id,media_id,url_link_low,url_link_std) "
		    		+ "VALUES(?,?,?,?)");
		    preparedStatement.setInt(1, photo.getEventId());
		    preparedStatement.setString(2, photo.getMediaId());
		    preparedStatement.setString(3, photo.getUrlLinkLow());
		    preparedStatement.setString(4, photo.getUrlLinkStd());
		    System.out.println(preparedStatement);	      
		    preparedStatement .executeUpdate();	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	 }
  }
  
  public void delete(int eventId, String mediaId) throws Exception {
	 if(check(eventId,mediaId))
	 try {
		  configure();
	      
	      preparedStatement = connection.prepareStatement("delete from photos "
	      		+ "where event_id = ? and media_id = ?");
	      preparedStatement.setInt(1, eventId);
	      preparedStatement.setString(2,mediaId);
	      preparedStatement.execute();
	      
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
}
  
  public ArrayList<Photo> findAll() throws Exception {
		ArrayList<Photo> photos = new ArrayList<Photo>();

		try {
			  configure();
		      statement = connection.createStatement();
		
		      resultSet = statement.executeQuery("select * from photos");
		      while (resultSet.next()) {
				  int eventId = resultSet.getInt("event_id");
				  String mediaId = resultSet.getString("media_id");
				  String urlLinkLow = resultSet.getString("url_link_low");
				  String urlLinkStd = resultSet.getString("url_link_std");
				  
				  Photo photo = new Photo(eventId,mediaId,urlLinkLow,urlLinkStd);
				  photos.add(photo);
		      }
		
	    } catch (Exception e) {
	      throw e;
	    } finally {
	      close();
	    }
	  	return photos;
	  }
  
  public ArrayList<Photo> findAllByEventId(int eventId) throws Exception {
			ArrayList<Photo> photos = new ArrayList<Photo>();

			try {
				  configure();
				  preparedStatement = connection.prepareStatement("select * "
				      		+ "from photos where event_id = ?");   
				      preparedStatement.setInt(1,eventId);
				      System.out.println(preparedStatement);
			          resultSet = preparedStatement.executeQuery();   
			          				  
			      while (resultSet.next()) {
					  String mediaId = resultSet.getString("media_id");
					  String urlLinkLow = resultSet.getString("url_link_low");
					  String urlLinkStd = resultSet.getString("url_link_std");
					  
					  Photo photo = new Photo(eventId,mediaId,urlLinkLow,urlLinkStd);
					  photos.add(photo);
			      }
			
		    } catch (Exception e) {
		      throw e;
		    } finally {
		      close();
		    }
		  	return photos;
		  }

	public void deleteAll() throws Exception {
		ArrayList<Photo> entities = this.findAll();
		for(Photo entity : entities)
			this.delete(entity.getEventId(),entity.getMediaId());
	}
} 