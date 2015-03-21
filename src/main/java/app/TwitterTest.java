package app;

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

import server.twitter.TwitterConnector;
import twitter4j.Status;
import app.repository.TweetRepository;
import entity.Event;
import entity.Venue;

@SpringBootApplication
@ComponentScan(basePackages = {"app.repository", "server.twitter"})
@EntityScan(basePackages = "app.models")
@EnableAutoConfiguration
public class TwitterTest implements CommandLineRunner {

	@Autowired 
	TwitterConnector twitter;
	
	private static final Logger log = LogManager.getLogger(TwitterTest.class);
	
	public void TweetsStreamTest(int tweetsize){
//		TwitterConnector twc = new TwitterConnector();
		double lat = 40.7143; 
		double lng = -74.006;
		for(Status tweet : twitter.TweetsStream(lat, lng, tweetsize, 2)){
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		ApplicationContext ctx = 
		SpringApplication.run(TwitterTest.class, args);
		
		
//		twc.StreamConcertTest();
		

	}
	
	@Override
    public void run(String... strings) throws Exception {
//		log.debug(tweet);
		log.debug(twitter);
		
		TweetsStreamTest(100);
		
//		twc.TweetsStreamTest(50);
	}

}
