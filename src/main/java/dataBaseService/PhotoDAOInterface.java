package dataBaseService;

import java.util.ArrayList;

import javabandsintown.entity.Artist;

public interface PhotoDAOInterface {

	public boolean check(int eventId, String mediaId) throws Exception;
	
//	public boolean existsPartecipations(String id, int event_id) throws Exception;
	
//	public Artist findById(String id) throws Exception;
	
	public void persist(Photo photo) throws Exception;
	
//	public void update(Artist artist) throws Exception;
	
	public void delete(int eventId, String mediaId) throws Exception;
	
	public ArrayList<Photo> findAll() throws Exception;
	
	public void deleteAll() throws Exception;
	
//	public ArrayList<Artist> getEventArtist(int eventId) throws Exception;

}