package twitter;

import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;

import org.joda.time.DateTime;

import search.Bandsintown;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entity.Event;

public class TwitterExample {
	
	public static ArrayList<Status> getTweetBylocation(double lat, double lng, int tweetSize) throws InterruptedException{
		System.out.println("Entering getTweetByLocation");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
		QueryResult results = null;
		
		Twitter twitter = TwitterFactory.getSingleton();
		
		Query query = new Query().geoCode(new GeoLocation(lat, lng), 2, "km");
//		query.setUntil("2014-11-29");
//		query.setSince("2014-12-05");
//		query.setUntil("2014-12-05");
//		Set<Status> tweets = new LinkedHashSet<Status>();
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		long lastID = Long.MAX_VALUE;
		
		int count = 100;
		
		if(tweetSize < count){
			count = tweetSize;
		}
		do {
			System.out.println("Entering getTweetByLocation");
			query.setCount(count);
			try {
				results = twitter.search(query);
				
			} catch (TwitterException e) {
				System.out.println(e.getErrorMessage());
				System.out.println(e.getRateLimitStatus().getSecondsUntilReset());
				System.out.println(e.getRetryAfter());
				e.printStackTrace();
				System.out.println("tweets retrieved: " + tweetsArrayList.size());
//				return tweetsArrayList;
//				Thread.sleep(e.getRateLimitStatus().getSecondsUntilReset() * 1000);
			}
			
//			tweets.addAll(results.getTweets());
			tweetsArrayList.addAll(results.getTweets());
			for (Status tweet: tweetsArrayList) {
		        if(tweet.getId() < lastID) 
		        	lastID = tweet.getId();
			}
			
			query.setMaxId(lastID-1);
		}while(tweetsArrayList.size() < count);
		
		return tweetsArrayList;
	}
	
	public static ArrayList<Status>getConcertTweets(Event event, int radius, JDBC db) throws InterruptedException, SQLException{
		System.out.println("Entering getTweetByLocation");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		DateTime start = new DateTime();
		QueryResult results = null;		
		
		Twitter twitter = TwitterFactory.getSingleton();
		
		Query query = new Query().geoCode(new GeoLocation(event.getVenue().getLatitude(), event.getVenue().getLongitude()), radius, "km");
		
		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		long lastID = 0;
		
		do {
			System.out.println("Entering getTweetByLocation");
			query.setCount(100);
			try {
				if(DateTime.now().isAfter(event.getDatetime().minusHours(2))){
					results = twitter.search(query);
					tweetsArrayList.addAll(results.getTweets());
				}
				
			} catch (TwitterException e) {
				System.out.println(e.getErrorMessage());
				System.out.println(e.getRateLimitStatus().getSecondsUntilReset());
				System.out.println(e.getRetryAfter());
				e.printStackTrace();
				System.out.println("tweets retrieved: " + tweetsArrayList.size());
				Thread.sleep(30000);
			}
		
			for (Status tweet: tweetsArrayList) {
				System.out.println();
				
		        if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
			}
			if(tweetsArrayList.size() > 0)
				db.testInsert(tweetsArrayList);
			System.out.println("retrieved: " + tweetsArrayList.size());
			tweetsArrayList.clear();
//			query.setMaxId(lastID-1);
			query.setSinceId(lastID);
			
			Thread.sleep(1000 * 10);
		}while(new DateTime().isBefore(event.getDatetime().plusHours(4)));
		
		return tweetsArrayList;

	}
	
	public static void main(String[] args) throws InterruptedException, URISyntaxException {
//		  
//		double lat = 40.7143; 
//		double lng = -74.006;
		
		
		Bandsintown bandsintown = new Bandsintown();
		JDBC s = null;
		ArrayList<Status> tweets = new ArrayList<Status>();
		
		ArrayList<Event> events = bandsintown.getEvents.setArtist("smashing pumpkins").setDate("2014-12-11").search();
		System.out.println(events.get(0));
//		DateTime now = new DateTime();
		
		System.out.println("Starting tweets retrieving: " + DateTime.now());
		System.out.println("Concert starts at: " + events.get(0).getDatetime());
		System.out.println("Concerts finished at: " + new DateTime("2014-12-12T05:00:00.000"));
		
		events.get(0).setDatetime(new DateTime("2014-12-12T05:00:00.000"));
		System.out.println(events.get(0).getDatetime());
		System.out.println(events.get(0).getDatetime().minusHours(2));
		System.out.println(events.get(0).getDatetime().plusHours(4));
		
		try{
			s = new JDBC("sqlite");
			tweets = getConcertTweets(events.get(0), 2, s);
		} catch(SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (s != null) s.closeConnection();
		}
		
		
		
		for(Status t : tweets){
			System.out.println(t.getCreatedAt() + " " + t.getGeoLocation());
		}
		
		
		
//		ArrayList<Status> tweets = getTweetBylocation(lat, lng, 50);
//		
//		for(Status tweet : tweets){		
//			System.out.println(tweet.getText());
//			System.out.println(tweet.getLang());
//			System.out.println(tweet.getCreatedAt());
//			System.out.println("---------------------------------------------");
//		}
//		
//		System.out.println("tweets received: " + tweets.size());
//		
//		
//		Bandsintown bandsintown = new Bandsintown();
//		Artist artist = ((ArtistGet) bandsintown.getArtist.setArtist("giovanni barra").setAppId().setApiVersion()).search();
//		System.out.println(artist);
////		
//		ArrayList<Event> events = ((EventsGet) ((EventsGet) bandsintown.getEvents.setArtist("marta sui tubi").setAppId().setApiVersion()).setDate("2014-12-01,2014-12-11")).search();
//		for (Event e : events){
//			System.out.println(e);
////			ArrayList<Status> t = getTweetBylocation(e.getVenue().getLatitude(), e.getVenue().getLongitude(), 5);
////			for(Status tweet : t){		
////				System.out.println(tweet.getText());
////				System.out.println(tweet.getLang());
////				System.out.println(tweet.getCreatedAt());
////				System.out.println("---------------------------------------------");
////			}
//		}
//		 
//		events.set(0, events.get(1));
//		
//		for(Event e : events){
//			System.out.println("Retrieving tweet for: " + e.getArtist().get(0).getName());
//			Twitter twitter = TwitterFactory.getSingleton();
//			Query query = new Query("martasuitubi").geoCode(new GeoLocation(e.getVenue().getLatitude(), e.getVenue().getLongitude()), 2, "km");
////			System.out.println(e.getDatetime().getChronology());
//			System.out.println(e.getDatetime().toString().substring(0, 10));
//			query.setSince(e.getDatetime().toString().substring(0,10));
//			query.setUntil(e.getDatetime().plusDays(1).toString().substring(0,10));
//			query.setCount(100);
//			
//			System.out.println(query.toString());
//			try {
//				for(Status s : twitter.search(query).getTweets()){
//					System.out.println("tweet: " + s.getCreatedAt() + " " + s.getText() + " " + s.getUser());
//				}
//			} catch (TwitterException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//		}
//		
//		
////		otter4java
////        Search searchTopsy = new Search();
////        
////        SearchResponse results = null;
////        try {
////                SearchCriteria criteria = new SearchCriteria();
////                criteria.setQuery("Maruti Gollapudi");
////
////                results = searchTopsy.search(criteria);
////                System.out.println(results.getResult().getList().size());
////                System.out.println(results.getResult().getTotal());
////        } catch (Otter4JavaException e) {
////                e.printStackTrace();
////        }
//		
//		
	}

}

