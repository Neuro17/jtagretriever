package dataBaseService;

import java.util.ArrayList;

public interface PhotoDAOInterface {

	public boolean check(int eventId, String mediaId) throws Exception;
		
	public void persist(Photo photo) throws Exception;
	
	public void delete(int eventId, String mediaId) throws Exception;
	
	public ArrayList<Photo> findAll() throws Exception;
	
	public void deleteAll() throws Exception;
	
}