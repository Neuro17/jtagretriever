package dataBaseService;

import java.util.ArrayList;

import entity.Artist;

public interface ArtistServiceIF {

	boolean exists(String id);
	
	Artist findById(String id);
	
	void persist(Artist artist);
	
	void update(Artist artist);
	
	void delete(String id);
	
	ArrayList<Artist> findAll();
	
	public void deleteAll();
	
	public ArrayList<Artist> getEventArtist(Integer eventId);

}
