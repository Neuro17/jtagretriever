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
		
		
//		PhotoRetriever pr = new PhotoRetriever();
//
//		double lat = 45.464161;
//		double lng = 9.190336;
//		DateTime start = (new DateTime()).minusHours(6);
//		DateTime end = (new DateTime());
//		Long radius = 4000L;
//		
//		String tag1 = "sun";
//		String tag2 = "newyork";
//		
//		int limit = 10;
//		
//		Instagram instagram = null;
//	    String CLIENT_ID = "f72f37aa491541a79412ce319f2e061f";
//	    
//	    instagram = new Instagram(CLIENT_ID);
//	    
//	    TagMediaFeed tmf = instagram.getRecentMediaTags(tag2);	    
//	    List<MediaFeedData> mfd = tmf.getData();
//	    
//	    log.debug(mfd.size());
//	    
//	    MediaFeed mf = instagram.getRecentMediaNextPage(tmf.getPagination());
//	    mfd.addAll(mf.getData());
//	    
//	    log.debug(mfd.size());
		
//		while(mediaList.size() < limit){
//			List<MediaFeedData> mediaListTmp = 
//					pr.getMediaByLocation(lat, lng, start, end, radius);
//			log.trace("tmp : " + mediaListTmp.size());
//			for(MediaFeedData mfd : mediaListTmp){
//				List<String> tags = mfd.getTags();
//				if(tags.contains(tag2)){
//					log.trace("valid mfd " + mfd.getLink());
//					mediaList.add(mfd);
//				}
//			}
//		}	

//		int i = 0;
//		for(MediaFeedData mfd : mediaList){
//        	log.debug(i + " " + mfd.getImages().getLowResolution().getImageUrl());
//        	i++;
//        }
		
		PhotoRetriever pr = new PhotoRetriever();
		double lat = 45.464161;
		double lng = 9.190336;
		DateTime start = (new DateTime()).minusDays(10);
		DateTime end = (new DateTime()).minusDays(5);
		Long radius = 4000L;
		
//		List<MediaFeedData> mfd = pr.getMediaByTag("newyork", 44);	
//		log.trace(mfd.size());
	
//		List<MediaFeedData> mfd = pr.getMediaByLocation(lat, lng,15 );
//		log.trace(mfd.size());
		
//		ArrayList<String> tagList = new ArrayList<String>();
//		tagList.add("mare");
//		tagList.add("spiaggia");
//		tagList.add("sole");
//		log.trace(pr.getMediaByTagList(tagList,5).size());

		String tag = "milano";
//		List<MediaFeedData> mfd = 
//				pr.getMedia(tag, lat, lng, start, end, radius, 53);
//		for(MediaFeedData media : mfd){
//			log.trace(media.getLink());
//			log.trace(new DateTime(Long.parseLong(media.getCreatedTime())* 1000));
//		}
//		log.trace(mfd.size());

		List<MediaFeedData> mediaList = pr.getMedia2(tag, lat, lng,start, end,radius, 55);
		for(MediaFeedData media : mediaList){
//			log.trace(media.getLink());
//			log.trace(new DateTime(Long.parseLong(media.getCreatedTime())* 1000));
		}

	}
}
