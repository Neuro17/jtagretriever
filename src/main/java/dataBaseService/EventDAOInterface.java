package dataBaseService;

import java.util.ArrayList;

import entity.Event;

public interface EventDAOInterface {

	  public boolean exists(Integer id) throws Exception;

	  public Event findById(Integer id) throws Exception;
	  
	  public void persist(Event entity) throws Exception;
	  
	  public void update(Event entity) throws Exception;
	  
	  public void delete(Integer id) throws Exception;
	  
	  public ArrayList<Event> findAll() throws Exception;
	  
	  public void deleteAll() throws Exception;
	  
}