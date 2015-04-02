package app.tools;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import search.Bandsintown;
import app.twitter.TwitterConnector;
import app.twitter.impl.TwitterConnectorImpl;
import dataBaseService.ArtistService;
import dataBaseService.EventService;
import dataBaseService.VenueService;
import entity.Artist;
import entity.Event;



/**
 * @author biagio
 * 
 * Utility class that performs init actions on databases and other useful actions
 *
 */
public class Task {
	
	private static final Logger log = LogManager.getLogger(Task.class);
	public static final String ARTISTS_FILE = "artists.txt";
	private static final ArtistService artDAO = new ArtistService();
	private static final EventService eventDAO = new EventService();
	private static final VenueService venueDAO = new VenueService();
	private static final Bandsintown bandsintown = new Bandsintown();
	private static final TwitterConnector twitterExtractor= new TwitterConnectorImpl();
	
	public static void initArtistDB(){
		ArrayList<String> artists = Tools.readFileFromResource(ARTISTS_FILE, "#");
		ArrayList<Artist> art = new ArrayList<Artist>();
		
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
	
	public static void initEventDB(){
		try {
			for(Artist artist : artDAO.findAll()) {
				
				log.debug(artist.getName());
				ArrayList<Event> events = bandsintown.getEvents.
						setArtist(artist.getName()).setDate("upcoming").searchGMTReferences();
				
				for (Event e : events) {
					eventDAO.persist(e);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void startTweetExtraction() {
		try {
			log.debug(eventDAO.findById(9069374));
			Event event = eventDAO.findById(9069374);
			
			twitterExtractor.StreamConcert(event, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		initArtistDB();
		initEventDB();
//		startTweetExtraction();
	}

}
