package twitter;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import javabandsintown.search.Bandsintown;
import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import app.tools.Tokenizer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import javabandsintown.entity.Event;

public class Test {
	
	private static final Logger log = LogManager.getLogger(Test.class);
	
	public static ArrayList<Status>getConcertTweets(Event event, int radius, JDBC db) throws InterruptedException, SQLException{
//		log.debug("Entering getTweetByLocation");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		DateTime start = new DateTime();
		QueryResult results = null;		
		
		Twitter twitter = TwitterFactory.getSingleton();
		
		Query query = new Query().geoCode(new GeoLocation(event.getVenue()
				.getLatitude(), event.getVenue().getLongitude()), radius, "km");

		ArrayList<Status> tweetsArrayList= new ArrayList<Status>();
		
		long lastID = 0;
		
		do {
//			log.debug("Entering getTweetByLocation");
			query.setCount(100);
			try {
				if(DateTime.now().isAfter(event.getDatetime().minusHours(2))){
					results = twitter.search(query);
					tweetsArrayList.addAll(results.getTweets());
				}
				
			} catch (TwitterException e) {
				log.debug(e.getErrorMessage());
				log.debug(e.getRateLimitStatus().getSecondsUntilReset());
				log.debug(e.getRetryAfter());
				e.printStackTrace();
				log.debug("tweets retrieved: " + tweetsArrayList.size());
				Thread.sleep(30000);
			}
		
			for (Status tweet: tweetsArrayList) {				
		        if(tweet.getId() > lastID) 
		        	lastID = tweet.getId();
			}
			if(tweetsArrayList.size() > 0)
				db.testInsert(tweetsArrayList);
//			log.debug("retrieved: " + tweetsArrayList.size());
			tweetsArrayList.clear();
//			query.setMaxId(lastID-1);
			query.setSinceId(lastID);
			
			Thread.sleep(1000 * 10);
		}while(new DateTime().isBefore(event.getDatetime().plusHours(4)));
		
		return tweetsArrayList;

	}
	
	public static ArrayList<String> filterTweets(Event e, JDBC db, double radius){
		ResultSet rs = null;
		String text = null;
		Double lat = null;
		Double lng =  null;
		LatLng center;
		LatLng other;
		double distance;
		ArrayList<String> filtered = new ArrayList<String>();
		
		
		try {
			rs = db.testSelect();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		try {
			while(rs.next()){
				text = rs.getString("text");
				lat = new Double(rs.getDouble("lat"));
				lng = new Double(rs.getDouble("lng"));
//				log.debug(lat + " " + lng + " " + text);

				center = new LatLng(e.getVenue().getLatitude(), e.getVenue().getLongitude());
				other = new LatLng(lat, lng);
				distance = LatLngTool.distance(center, other, LengthUnit.KILOMETER);
				if(distance <= radius)
					filtered.add(rs.getString("text"));
//				log.debug(distance);
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return filtered;
	}
	
	public static void main(String[] args) throws IOException{
		Map<String, Integer> countWords = new HashMap<String, Integer>();
		Bandsintown bandsintown = new Bandsintown();
		JDBC s = null;
		ArrayList<String> filtered = null;
		
//		ArrayList<Status> tweets = new ArrayList<Status>();
		
		ArrayList<Event> events = bandsintown.getEvents.setArtist("smashing pumpkins").setDate("2014-12-11").search();
//		log.debug(events.get(0));
		
		try {
			s = new JDBC("sqlite");
			filtered = filterTweets(events.get(0), s, 0.5);
			log.debug("reading from DB - done");
		} catch (ClassNotFoundException  e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			s.closeConnection();
		}
		
//		log.debug(filtered.size());
		
		countWords = new Tokenizer().tokenize(filtered);
		log.debug(countWords.get("smashing"));
//		Map<String, Integer> a = new LinkedHashMap<String, Integer>();
		for (Entry<String, Integer> entry : countWords.entrySet()) {
			
//			log.info(entry.getKey() + "," + entry.getValue());
			
			if(entry.getValue() > 3)
				log.info(entry.getKey() + "," + entry.getValue());
		}
		
		
		
	}

}
