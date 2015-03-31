package app.twitter.impl;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//import scala.annotation.meta.getter;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.Query.Unit;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import app.models.Tweet;
import app.models.TweetKey;
import app.repository.TweetRepository;
import app.twitter.TwitterConnector;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.Event;

@Component
public class TwitterConnectorImpl implements TwitterConnector{

	private static final Logger log = LogManager.getLogger(Twitter.class);
	private static final int DEFAULT_COUNT = 100;
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public int queryCount;
	public Query query;
	private Twitter twitter;
	
	@Autowired
	TweetRepository twitterRepo;
	
//	public TwitterConnectorImpl(int queryCount) {
//		this.queryCount = queryCount;
//		this.twitter = TwitterFactory.getSingleton();
//	}
	
	public TwitterConnectorImpl() {
		this.queryCount = DEFAULT_COUNT;
		this.twitter = TwitterFactory.getSingleton();
	}

	public int getQueryCount() {
		return queryCount;
	}

	public void setQueryCount(int queryCount) {
		this.queryCount = queryCount;
	}
	
	public Query getQuery() {
		return query;
	}

	public void setQuery(Query query) {
		this.query = query;

	}
	
	private ArrayList<Status> getTweet(){
		QueryResult results = null;
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		try {
			results = twitter.search(query);
			
		} catch (TwitterException e) {
			log.error(e.getErrorMessage());
			log.error(e.getRateLimitStatus().getSecondsUntilReset());
			log.error(e.getRetryAfter());
			e.printStackTrace();
			log.error("tweets retrieved: " + tweetsArrayList.size());
//			Thread.sleep(e.getRateLimitStatus().getSecondsUntilReset() * 1000);
		}
		
		tweetsArrayList.addAll(results.getTweets());
		if(tweetsArrayList.isEmpty()){
			log.warn("No results found for query " + query.toString());
		}
		return tweetsArrayList;
	}
	
	public long untilNow(ArrayList<Status> tweets, long lastID) {
		for (Status tweet: tweets) {
	        if(tweet.getId() < lastID) 
	        	lastID = tweet.getId();
		}
		return lastID - 1;
	}
	
	public long sinceNow(ArrayList<Status> tweets, long lastID) {
//		log.debug(lastID);
		for (Status tweet: tweets) {
			 if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
		}
		return lastID;
	}
	
	public ArrayList<Status> TweetsStream(double lat, double lng, int tweetsize,
			int radius){
	
		log.trace("Entering TweeetsStream");
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		queryCount = tweetsize < queryCount ? tweetsize : queryCount;	
		
//		QueryResult results = null;
		
		query = new Query().geoCode(new GeoLocation(lat, lng), radius,
				Unit.km.toString());
		
		long lastID = Long.MAX_VALUE;

		ArrayList<Status> tmpTweets= new ArrayList<Status>();
		
		do {
			tmpTweets = getTweet();
			tweetsArrayList.addAll(tmpTweets);
			
			for (Status tweet: tmpTweets) {
				
				TweetKey pk = new TweetKey();
	        	pk.setEventName("test event");
	        	pk.setId(tweet.getId());
	        	
	        	log.debug(twitterRepo);
	        	
	        	twitterRepo.save( new Tweet(pk, tweet.getGeoLocation().getLatitude(),
	        			tweet.getGeoLocation().getLongitude()));
				
		        if(tweet.getId() < lastID) 
		        	lastID = tweet.getId();
			}
			
			tmpTweets.clear();
			query.setMaxId(lastID - 1);
			
			
		} while(tweetsArrayList.size() < queryCount);
		
		return tweetsArrayList;
		
	}
	
	public ArrayList<Status> StreamConcert(Event event, int radius) 
			throws InterruptedException {
				
		log.trace("Entering StreamConcert");
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		double lat = event.getVenue().getLatitude();
		double lng = event.getVenue().getLongitude();
		
		DateTime startDate = event.getDatetime().minusHours(2);
		DateTime endDate = event.getDatetime().plusHours(4);
		
		log.debug(getQueryCount());
		
		query = new Query().geoCode(new GeoLocation(lat, lng), radius, "km");
		
//		query = new Query().geoCode(new GeoLocation(lat, lng), radius,
//				Unit.km.toString()
		
		long lastID = 0;

		do {
			ArrayList<Status> tmpTweets= new ArrayList<Status>();
			
			if(DateTime.now().isAfter(startDate)){
				log.debug("Starting tweets stream");
				tmpTweets = getTweet();
				log.debug("retrieved " + tmpTweets.size() + " tweets");
				tweetsArrayList.addAll(tmpTweets);
			}
			
			else {
				log.debug("too early");
				Thread.sleep(1000 * 60);
			}
			
			for (Status tweet: tmpTweets) {
				TweetKey pk = new TweetKey();
//				log.debug("Ho trovato dei tweet, ora dovrei salvarli!!!!");
	        	pk.setEventName(event.getTitle());
	        	pk.setId(tweet.getId());
//	        	
//	        	log.debug(twitterRepo);
//	        	TODO - ricordarsi di inserire tutti i campi necessari per la tabella
 	        	twitterRepo.save( new Tweet(pk, tweet.getGeoLocation().getLatitude(),
	        			tweet.getGeoLocation().getLongitude()));
		        if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
			}
			
			tmpTweets.clear();
//			log.debug(lastID);
			
			query.setSinceId(lastID);
			Thread.sleep(1000 * 10);
		} while(DateTime.now().isBefore(endDate));
		
		return tweetsArrayList;
		
	}
	
	
	
}
