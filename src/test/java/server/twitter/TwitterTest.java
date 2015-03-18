package server.twitter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import entity.Event;
import entity.Venue;
import server.twitter.TwitterConnector;
import twitter4j.Status;

public class TwitterTest {

	private static final Logger log = LogManager.getLogger(TwitterTest.class);
	
	public void TweetsStreamTest(int tweetsize){
		TwitterConnector twc = new TwitterConnector();
		double lat = 40.7143; 
		double lng = -74.006;
		for(Status tweet : twc.TweetsStream(lat, lng, tweetsize, 2)){
			log.info(tweet.getText());
			log.info(tweet.getLang());
			log.info(tweet.getCreatedAt());
			log.info(tweet.getGeoLocation().getLatitude());
			log.info("---------------------------------------------");
		}
		
		
	}
	
	public void StreamConcertTest() throws InterruptedException{
		TwitterConnector twc = new TwitterConnector();
		double lat = 40.7833; 
		double lng = 73.9667;
		DateTime start = DateTime.now();
		
		Event event = new Event();
		event.setDatetime(start);
		event.setVenue(new Venue(lat, lng));
		
		twc.StreamConcert(event, 2);
		
//		for(Status tweet : twc.StreamConcert(event, 2)){
//			log.info(tweet.getText());
//			log.info(tweet.getLang());
//			log.info(tweet.getCreatedAt());
//			log.info(tweet.getGeoLocation().getLatitude());
//			log.info("---------------------------------------------");
//		}
		
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		
		TwitterTest twc = new TwitterTest();
		
		twc.TweetsStreamTest(50);
		
//		twc.StreamConcertTest();
		

	}

}
