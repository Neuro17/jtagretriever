package app;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import twitter4j.Status;
import app.twitter.TwitterConnector;
import app.tools.TagExtractor;
import dataBaseService.EventService;
import entity.Event;
import entity.Venue;

@SpringBootApplication
@ComponentScan(basePackages = {"app.repository",  "app.twitter", "app.tools", "app.twitter.impl"})
@EntityScan(basePackages = "app.models")
@EnableAutoConfiguration
public class TwitterTest implements CommandLineRunner {

	@Autowired 
	TwitterConnector twitter;
	
	@Autowired 
	TagExtractor TagExtractor;
	
	private static final EventService eventDAO = new EventService();
	
	private static final Logger log = LogManager.getLogger(TwitterTest.class);
	
	public void TweetsStreamTest(int tweetsize){
//		TwitterConnector twc = new TwitterConnector();
		double lat = 40.7143; 
		double lng = -74.006;
		for(Status tweet : twitter.TweetsStream(lat, lng, tweetsize, 2)){
			log.info(tweet.getText());
			log.info(tweet.getLang());
			log.info(tweet.getCreatedAt());
//			log.info(tweet.getGeoLocation().getLatitude());
			log.info("---------------------------------------------");
		}
		
		
	}
	
	public void StreamConcertTest() throws InterruptedException{
//		double lat = 40.7143; 
//		double lng = -74.006;
//		DateTime start = DateTime.now();
//		
//		Event event = new Event();
//		event.setDatetime(start);
//		event.setVenue(new Venue(lat, lng));
//		event.setTitle("test event 2");
		
//		Event event;
		try {
			Event event = eventDAO.findById(9069374);
			twitter.StreamConcert(event, 2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
//		for(Status tweet : twc.StreamConcert(event, 2)){
//			log.info(tweet.getText());
//			log.info(tweet.getLang());
//			log.info(tweet.getCreatedAt());
//			log.info(tweet.getGeoLocation().getLatitude());
//			log.info("---------------------------------------------");
//		}
		
		
	}
	
	public void TagExtractorTest() {
		double lat = 40.7143; 
		double lng = -74.006;
		
		DateTime start = DateTime.now();
		
		Event event = new Event();
		event.setDatetime(start);
		event.setVenue(new Venue(lat, lng));
		event.setTitle("test event");

//		Event event;
		try {
//			event = eventDAO.findById(9069374);
			for (Map.Entry<String, Integer> entry : TagExtractor.extracxtTag(event, 0.5).entrySet()) {
			    log.debug(entry.getKey() + " : " + entry.getValue());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(TwitterTest.class, args);
	}
	
	@Override
    public void run(String... strings) throws Exception {
//		log.debug(tweet);
		log.debug(twitter);
		
		TweetsStreamTest(100);
		
//		StreamConcertTest();
		
//		TagExtractorTest();
	}
}
