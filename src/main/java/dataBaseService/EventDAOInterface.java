package dataBaseService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.joda.time.LocalDate;

import javabandsintown.entity.Event;

public interface EventDAOInterface {

	  public boolean exists(Integer id) throws Exception;
	
	  public boolean checkName(String eventName) throws Exception;

	  public Event findById(Integer id) throws Exception;
	  
	  public ArrayList<Event> findByName(String eventName) throws Exception;
	  
	  public void persist(Event entity) throws Exception;
	  
	  public void update(Event entity) throws Exception;
	  
	  public void delete(Integer id) throws Exception;
	  
	  public ArrayList<Event> findAll() throws Exception;
	  
	  public void deleteAll() throws Exception;	  
	  
	  public ArrayList<Event> getTodaysEvents(LocalDate today) throws Exception;
}