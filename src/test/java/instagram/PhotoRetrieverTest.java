package instagram;

import java.util.ArrayList;
import java.util.List;

import javabandsintown.geonames.GeonamesConnector;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.joda.time.DateTime;

import scala.annotation.meta.getter;
import app.instagram.PhotoRetriever;

public class PhotoRetrieverTest {
	
	private final static Logger log = LogManager.getLogger(PhotoRetrieverTest.class);

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
		
		List<MediaFeedData> mediaFeeds = pr.getMediaByLocation(40.033333, 18.133333,y,t,radius,4);
				
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
		
        List<MediaFeedData> mediaList = pr.getMediaByTag(tag);
        
        for(MediaFeedData mfd : mediaList){
        	System.out.println(mfd.getImages().getLowResolution().getImageUrl());
        }
        
        
	}
	
	public static void main(String args[]) throws InstagramException{

//		mainTest();

//		searchPageTest();
		
		double lat = 45.464161;
		double lng = 9.190336;
		DateTime start = (new DateTime()).minusDays(6);
		DateTime end = (new DateTime().minusDays(3));
		Long radius = 5000L;
		String tag = "expo";
				
		PhotoRetriever pr = new PhotoRetriever();
		
		List<MediaFeedData> medias = pr.getMedia2(tag, lat, lng, start, end, radius,50);
		log.trace("final size " + medias.size());
//		List<MediaFeedData> mediaList = pr.getMedia2(tag, lat, lng,start, end,radius, 55);
//		for(MediaFeedData media : mediaList){
//			log.trace(media.getLink());
//			log.trace(new DateTime(Long.parseLong(media.getCreatedTime())* 1000));
//		}

	}
}
