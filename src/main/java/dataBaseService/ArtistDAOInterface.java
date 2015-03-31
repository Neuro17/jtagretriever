package dataBaseService;

import java.util.ArrayList;

import entity.Artist;

public interface ArtistDAOInterface {

	public boolean exists(String id)  throws Exception;
	
	public boolean existsPartecipations(String id, int event_id) throws Exception;
	
	public Artist findById(String id) throws Exception;
	
	public void persist(Artist artist) throws Exception;
	
	public void update(Artist artist) throws Exception;
	
	public void delete(String id) throws Exception;
	
	public ArrayList<Artist> findAll() throws Exception;
	
	public void deleteAll() throws Exception;
	
	public ArrayList<Artist> getEventArtist(int eventId) throws Exception;

}