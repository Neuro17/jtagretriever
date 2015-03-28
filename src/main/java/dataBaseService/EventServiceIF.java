package dataBaseService;

import java.util.ArrayList;

import entity.Event;

public interface EventServiceIF {

	  public boolean exists(Integer id);

	  public Event findById(Integer id);
	  
	  public void persist(Event entity);
	  
	  public void update(Event entity);
	  
	  public void delete(Integer id);
	  
	  public ArrayList<Event> findAll();
	  
	  public void deleteAll();
	  
}
