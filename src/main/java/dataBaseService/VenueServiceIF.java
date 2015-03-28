package dataBaseService;

import java.util.ArrayList;

import entity.Venue;

public interface VenueServiceIF {
	
	  public boolean exists(Integer id);
	  
	  public Venue findById(Integer id);
	  
	  public void persist(Venue entity);
	  
	  public void update(Venue entity);
	  
	  public void delete(Integer id);

	  public ArrayList<Venue> findAll();

	  public void deleteAll();

}
