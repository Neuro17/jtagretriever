package app.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

import javabandsintown.search.Bandsintown;
import app.instagram.PhotoRetriever;
import app.twitter.TwitterConnector;
import app.twitter.impl.TwitterConnectorImpl;
import dataBaseService.ArtistDAOInterface;
import dataBaseService.ArtistService;
import dataBaseService.EventService;
import dataBaseService.Photo;
import dataBaseService.PhotoService;
import dataBaseService.VenueService;
import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;

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
	private static final PhotoService photoDAO = new PhotoService();
	private static final Bandsintown bandsintown = new Bandsintown();
	private static final TwitterConnector twitterExtractor = new TwitterConnectorImpl();
	
	public static String complete(int i){
		if(i < 10)
			return "0" + Integer.toString(i);
		else
			return Integer.toString(i);
	}
	
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
						setArtist(artist.getName()).setDate("all").searchGMTReferences();
				
				for (Event e : events) {
					eventDAO.persist(e);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateDBAritsts() throws Exception{
		ArrayList<Artist> artists = artDAO.findAll();
		
		for(Artist a : artists){
			Artist updatedArtist = 
					bandsintown.getArtist.setArtist(a.getName()).search();
			artDAO.update(updatedArtist);
		}
	}
	
	public static void startTweetExtraction() {
		try {
			log.debug(eventDAO.findById(9069374));
			Event event = eventDAO.findById(9069374);
			
			twitterExtractor.StreamConcert(event, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void addArtistToFile(String artistName) {
		boolean found = false;
		ArrayList<String> artists = Tools.readFileFromResource(ARTISTS_FILE, "#");
		for(String artist : artists){
			if(artist.equalsIgnoreCase(artistName)) {
				log.debug(artist);
				found = true;
			}
		}
		if(!found) {
			Tools.appendFileFromResource(ARTISTS_FILE, artistName);
		}
	}
	
	public static void initUpcomingArtistEvents(String artistName){
		try {
				Artist artist = artDAO.findById(artistName);
				log.debug(artist.getName());
				ArrayList<Event> events = bandsintown.getEvents.
						setArtist(artist.getName()).setDate("upcoming").searchGMTReferences();
				log.trace(events.size() + " events for artist " + artist.getName());		
				for (Event e : events) {
					eventDAO.persist(e);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void updateArtistsEvents() throws Exception{
		ArtistService aS = new ArtistService();
		
		ArrayList<Artist> artists = aS.findAll(); 
		for(Artist a : artists){
			log.trace(a);
			initUpcomingArtistEvents(a.getName());
		}
	}
	
	public static void updateArtistsEvents(DateTime start,DateTime end) 
			throws Exception{		
		ArrayList<Artist> artists = artDAO.findAll();
		
		String timeForwardString = end.getYear() + "-" + 
				complete(end.getMonthOfYear()) + "-" + 
				complete(end.getDayOfMonth());
		String timeAgoString = start.getYear() + "-" + 
				complete(start.getMonthOfYear()) + "-" + 
				complete(start.getDayOfMonth());
		String datesString = timeAgoString + "," + timeForwardString; 

		for(Artist a : artists){
			log.trace(a);
			ArrayList<Event> events = bandsintown.getEvents
				.setArtist(a.getName())
				.setDate(datesString).search();
			if(events != null){
				log.trace(events.size());
				for(Event e : events)
					eventDAO.persist(e);
			}
		}
	}
	
	public static void collectEventPhotos(int eventId) throws Exception{
		PhotoRetriever pr = new PhotoRetriever();
		TagExtractorImp tagExtractor = new TagExtractorImp();
		
		Event event = eventDAO.findById(eventId);
		
		ArrayList<String> tags = tagExtractor.extractTagFromBandsintown(event);

		List<MediaFeedData> medias = pr.getMedia4tris(tags, 
				event.getVenue().getLatitude(), event.getVenue().getLongitude(),
				event.getDatetime().minusHours(1), event.getDatetime().plusHours(11),
				5000L);
		log.trace(medias.size());

		Photo p = new Photo();

		for(MediaFeedData media : medias){
			p.setEventId(eventId);
			p.setMediaId(media.getId());
			p.setUrlLinkLow(media.getImages().getLowResolution().getImageUrl());
			p.setUrlLinkStd(media.getImages().getStandardResolution().getImageUrl());
		
			photoDAO.persist(p);			
		}
	}
	
	public static void collectDaysAgoDBEventsPhotos(int days) throws Exception{
		LocalDate localDate = (new LocalDate()).minusDays(days);
		ArrayList<Event> eventsToManage = new ArrayList<Event>();
		  
		ArrayList<Event> events = eventDAO.getTodaysEvents(localDate);			
		log.trace(events.size());

		for(Event e : events)
			if(!photoDAO.existsAlmostsOne(e.getId())
//					&&(e.getId() > 10035741)
					)
				  eventsToManage.add(e);
		log.trace(eventsToManage.size());

		for(Event e : eventsToManage){
			log.trace(e);
			collectEventPhotos(e.getId());
		}
	}
	
	public static void main(String[] args) throws Exception {
		DateTime start = (new DateTime()).minusDays(7);
		DateTime end = (new DateTime()).plusDays(7);
		
//		initArtistDB();
//		initEventDB();
//		startTweetExtraction();
//		initUpcomingArtistEvents("Calvin Harris");
//		collectEventPhotos(8932897);
		collectDaysAgoDBEventsPhotos(1);
// last update 2015-06-11
//		updateArtistsEvents();
//		updateArtistsEvents(start, end);
	}

}
