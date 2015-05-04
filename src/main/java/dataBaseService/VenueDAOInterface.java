package dataBaseService;

import java.util.ArrayList;

import entity.Venue;

public interface VenueDAOInterface {
	
	  public boolean exists(double lat,double lng) throws Exception;
	  
	  public boolean checkName(String venueName) throws Exception;
	  
	  public Venue find(double lat,double lng) throws Exception;
	  
	  public ArrayList<Venue> findByName(String venueName) throws Exception;
	  
	  public void persist(Venue entity) throws Exception;
	  
	  public void update(Venue entity) throws Exception;
	  
	  public void delete(double lat,double lng) throws Exception;

	  public ArrayList<Venue> findAll() throws Exception;

	  public void deleteAll() throws Exception;

}