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
	private static final VenueService venueDAO = new VenueService();
	private static final Bandsintown bandsintown = new Bandsintown();
	private static final TwitterConnector twitterExtractor= new TwitterConnectorImpl();
		
	public static void initArtistDB(){
		ArrayList<String> artists = Tools.readFileFromResource(ARTISTS_FILE, "#");
		ArrayList<Artist> art = new ArrayList<Artist>();
		ArtistService artService = new ArtistService();
		
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
				
				for (Event e : events) {
					eventDAO.persist(e);
				}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void collectEventPhotos(int eventId) throws Exception{
		EventService eS = new EventService();
		PhotoRetriever pr = new PhotoRetriever();
		PhotoService pS = new PhotoService();
		TagExtractorImp tagExtractor = new TagExtractorImp();
		
		Event event = eS.findById(eventId);
//log.trace(event);
		
		ArrayList<String> tags = tagExtractor.extractTagFromBandsintown(event);
//log.trace(tags);

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
		
			pS.persist(p);			
//log.trace(new DateTime(Long.parseLong(media.getCreatedTime())*1000));
		}
	}
	
	public static void collectYesterdayDBEventsPhotos() throws Exception{
		EventService eS = new EventService();
		LocalDate localDate = (new LocalDate()).minusDays(8);
		PhotoService pS = new PhotoService();
		ArrayList<Event> eventsToManage = new ArrayList<Event>();
		  
		ArrayList<Event> events = eS.getTodaysEvents(localDate);			
log.trace(events.size());

		for(Event e : events)
			if(!pS.existsAlmostsOne(e.getId()))
				  eventsToManage.add(e);
log.trace(eventsToManage.size());

		for(Event e : eventsToManage){
log.trace(e);
			if(e.getId() != 9900839
					&& e.getId() != 9187059
					&& e.getId() != 9326508
					&& e.getId() != 9700304
					&& e.getId() != 9095063
					&& e.getId() != 9848450
					)
				collectEventPhotos(e.getId());
		}
	}
	
	public static void main(String[] args) throws Exception {
//		initArtistDB();
//		initEventDB();
//		startTweetExtraction();
//		initUpcomingArtistEvents("Calvin Harris");
//		collectEventPhotos(8932897);
		collectYesterdayDBEventsPhotos();
	}

}
