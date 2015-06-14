package app.twitter.impl;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

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

import javabandsintown.entity.Event;

/**
 * @author neuro
 *
 */
@Service
public class TwitterConnectorImpl implements TwitterConnector{

	private static final Logger log = LogManager.getLogger(Twitter.class);
	private static final int DEFAULT_COUNT = 100;
	
	@SuppressWarnings("unused")
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public int queryCount;
	public Query query;
	private Twitter twitter;
	public boolean running;
	
	@Autowired
	TweetRepository twitterRepo;
		
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
	
	/**
	 * Method that performs request to twitter using twitter4j library
	 * @return
	 */
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
	
	@Deprecated
	public long untilNow(ArrayList<Status> tweets, long lastID) {
		for (Status tweet: tweets) {
	        if(tweet.getId() < lastID) 
	        	lastID = tweet.getId();
		}
		return lastID - 1;
	}
	
	@Deprecated
	public long sinceNow(ArrayList<Status> tweets, long lastID) {
		for (Status tweet: tweets) {
			 if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
		}
		return lastID;
	}
	
	
	/* (non-Javadoc)
	 * @see app.twitter.TwitterConnector#TweetsStream(double, double, int, int)
	 */
	public ArrayList<Status> TweetsStream(double lat, double lng, int tweetsize,
			int radius){
	
		log.trace("Entering TweeetsStream");
		log.debug(twitterRepo);
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		queryCount = tweetsize < queryCount ? tweetsize : queryCount;	
				
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
	        	
	        	log.debug(pk);
	        	log.debug("stampo geolocation: " + tweet.getGeoLocation());
	        	
	        	/*
	        	 * a volte tweet.getGeoLocation torna null anche se nella query
	        	 *  viene impostato il parmaetro per raccogliere solo i tweets
	        	 *  per una data lat e lng
	        	 * */
	        	
	        	if(tweet.getGeoLocation() != null) {
	        		Tweet tmpTweet = new Tweet(pk, 
		        			tweet.getGeoLocation().getLatitude(),
		        			tweet.getGeoLocation().getLongitude(),
		        			tweet.getText(),
		        			tweet.getPlace().getName(),
		        			tweet.getCreatedAt().toString());
	        		twitterRepo.save(tmpTweet);
	        	}
	        
		        if(tweet.getId() < lastID) 
		        	lastID = tweet.getId();
			}
			
			if(tmpTweets.size() > 0 ) {
				tmpTweets.clear();
			}
			query.setMaxId(lastID - 1);
			
			
		} while(tweetsArrayList.size() < queryCount);
		
		return tweetsArrayList;
		
	}
	
	/* (non-Javadoc)
	 * @see app.twitter.TwitterConnector#StreamConcert(entity.Event, int)
	 */
	@Async
	public ArrayList<Status> StreamConcert(Event event, int radius) 
			throws InterruptedException {
				
		log.trace("Entering StreamConcert for events: " + event.getTitle());
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		double lat = event.getVenue().getLatitude();
		double lng = event.getVenue().getLongitude();
		
		DateTime startDate = event.getDatetime().minusHours(2);
		DateTime endDate = event.getDatetime().plusHours(4);
		
		log.debug(getQueryCount());
		
		query = new Query().geoCode(new GeoLocation(lat, lng), radius, "km");
				
		long lastID = 0;

		do {
			ArrayList<Status> tmpTweets= new ArrayList<Status>();
			DateTime now = DateTime.now();
			
			if(now.isAfter(startDate)){
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
				if(tweet.getGeoLocation() == null){
					continue;
				}
				TweetKey pk = new TweetKey();
				log.debug("Ho trovato dei tweet, ora dovrei salvarli!!!!");
	        	pk.setEventName(event.getTitle());
	        	pk.setId(tweet.getId());
	        	
	        	Tweet tmpTweet = new Tweet(pk, 
	        			tweet.getGeoLocation().getLatitude(),
	        			tweet.getGeoLocation().getLongitude(),
	        			tweet.getText(),
	        			tweet.getPlace().getName(),
	        			tweet.getCreatedAt().toString());

//	        	TODO - ricordarsi di inserire tutti i campi necessari per la tabella
	        	System.out.println("twitterRepo: " + twitterRepo);
	        	System.out.println("tweets: " + tmpTweet.toString());
 	        	twitterRepo.save(tmpTweet);
		        if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
			}
			
			tmpTweets.clear();
			
			query.setSinceId(lastID);
			Thread.sleep(1000 * 10);
		} while(DateTime.now().isBefore(endDate));
		
		return tweetsArrayList;		
	}
}
