package instagram;

import java.util.ArrayList;
import java.util.List;

import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;
import javabandsintown.geonames.GeonamesConnector;
import javabandsintown.search.Bandsintown;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.joda.time.DateTime;

import dataBaseService.ArtistService;
import scala.annotation.meta.getter;
import app.instagram.PhotoRetriever;
import app.tools.TagExtractorImp;

public class PhotoRetrieverTest {
	
	private final static Logger log = LogManager.getLogger(PhotoRetrieverTest.class);
//
	public static void mainTest() throws InstagramException{
		List<String> tagList = new ArrayList<String>();
		tagList.add("pappa");
		tagList.add("cacca");
		tagList.add("nanna");
		tagList.add("pearljam");

		PhotoRetriever pr = new PhotoRetriever();
		
		DateTime y = new DateTime();	
		y = y.minusMonths(3);
//		System.out.println(y);

		DateTime t = new DateTime();
		t = t.minusMonths(2);
//		System.out.println(t);
		
		Long radius = 1000L;
		
		List<MediaFeedData> mediaFeeds = new ArrayList<MediaFeedData>(); 
//				pr.getMediaByLocation(40.033333, 18.133333,y,t,radius,4);
				
		for(MediaFeedData mfd : mediaFeeds){
			long sec = Long.parseLong(mfd.getCreatedTime());
			System.out.println(mfd.getLink() + "\n" + new DateTime(sec*1000) + "\n" + mfd.getLocation());				
//			tagList = mfd.getTags();
//				
//			for(String tag : tagList)
//				System.out.println(tag);

			System.out.println();
		}
	}
	
	public static void searchPageTest() throws InstagramException{
		
		PhotoRetriever pr = new PhotoRetriever();
		
		String tag = "gold";
		
        List<MediaFeedData> mediaList = new ArrayList<MediaFeedData>(); 
//        		pr.getMediaByTag(tag);
        
        for(MediaFeedData mfd : mediaList){
        	System.out.println(mfd.getImages().getLowResolution().getImageUrl());
        }
        
        
	}
	
	public static void main(String args[]) throws Exception{

//		mainTest();

//		searchPageTest();
		
//		double lat = 45.464161;
//		double lng = 9.190336;
//		DateTime start = (new DateTime()).minusDays(6);
//		DateTime end = (new DateTime().minusDays(3));
//		Long radius = 5000L;
		String tag = "Classixx";
				
		PhotoRetriever pr = new PhotoRetriever();
		Bandsintown bandsintown = new Bandsintown();
		
		Artist a = bandsintown.getArtist.setArtist(tag).search();
log.trace(a);

		DateTime now = new DateTime();
		DateTime timeAgo = now.minusMonths(6);
		String nowString = now.getYear() + "-" + complete(now.getMonthOfYear()) + "-" + complete(now.getDayOfMonth());
		String timeAgoString = timeAgo.getYear() + "-" + complete(timeAgo.getMonthOfYear()) + "-" + complete(timeAgo.getDayOfMonth());
		String datesString = timeAgoString + "," + nowString; 

		ArrayList<Event> events = bandsintown.getEvents
				.setArtist(a.getName())
				.setDate(datesString).search();
log.debug("total events on bandsintown " + events.size());

		Event event = events.get(0);
log.trace("event selected " + event);

		TagExtractorImp tagE = new TagExtractorImp();
		ArrayList<String> tags = tagE.extractTagFromBandsintown(event);
		
		List<MediaFeedData> medias = pr.getMedia4tris(tags, 
				event.getVenue().getLatitude(), event.getVenue().getLongitude(),
				event.getDatetime().minusHours(12), 
				event.getDatetime().plusHours(18), 
				5000L);
		log.trace("final size " + medias.size());
//		List<MediaFeedData> mediaList = pr.getMedia2(tag, lat, lng,start, end,radius, 55);
//		for(MediaFeedData media : mediaList){
//			log.trace(media.getLink());
//			log.trace(new DateTime(Long.parseLong(media.getCreatedTime())* 1000));
//		}

	}
	
	private static String complete(int i){
		if(i < 10)
			return  "0" + Integer.toString(i);
		else
			return
					Integer.toString(i);
	}

}
