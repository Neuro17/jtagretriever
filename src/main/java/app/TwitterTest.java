package app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import twitter4j.Status;
import app.tools.TagExtractor;
import app.twitter.TwitterConnector;
import dataBaseService.EventService;
import javabandsintown.entity.Event;

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
		double lat = 40.7143; 
		double lng = -74.006;
		for(Status tweet : twitter.TweetsStream(lat, lng, tweetsize, 2)){
			log.info(tweet.getText());
			log.info(tweet.getLang());
			log.info(tweet.getCreatedAt());
			log.info("---------------------------------------------");
		}
		
		
	}
	
	public void StreamConcertTest() throws InterruptedException{
		try {
			Event event = eventDAO.findById(9794397);
			twitter.StreamConcert(event, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void TagExtractorTest() throws Exception {
		EventService es = new EventService();
		Event e = es.findById(9069374);

		for (String tag : TagExtractor.extractTag(e, 1)) {
			log.debug(tag);
		}
		
	}
	
	public static void main(String[] args) {
		
		SpringApplication.run(TwitterTest.class, args);
	}
	
	@Override
    public void run(String... strings) throws Exception {
//		log.debug(tweet);
//		log.debug(twitter);
//		TweetsStreamTest(100);
		StreamConcertTest();
//		TagExtractorTest();
	}
}
