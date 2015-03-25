package app.tools;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import search.Bandsintown;
import app.models.interfaces.ArtistDAOInterface;
import dataBaseService.ArtistService;
import entity.Artist;

public class Task {
	
	private static final Logger log = LogManager.getLogger(Task.class);
	public static final String ARTISTS_FILE = "artists.txt";
	
	public void allineaTweet(){
		
	}
	
	public static void main(String[] args) {
		ArrayList<String> artists = Tools.readFileFromResource(ARTISTS_FILE, "#");
		ArrayList<Artist> art = new ArrayList<Artist>();
		Bandsintown bandsintown = new Bandsintown();
		ArtistService artDAO = new ArtistService();
		
		log.debug(artDAO.getJdbcDriver());
		log.debug(artDAO.getDbUrl());
		log.debug(artDAO.getUser());
		log.debug(artDAO.getPass());
		
		for(String artist : artists) {
			Artist artTmp = bandsintown.getArtist.setArtist(artist).search();
			art.add(artTmp);
		}
		
		for(Artist artist : art) {
			log.debug(artist.toString());
			try {
				artDAO.persist(artist);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
