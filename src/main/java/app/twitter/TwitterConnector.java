package app.twitter;

import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.Status;
import entity.Event;

public interface TwitterConnector {
	
	public int getQueryCount();
	
	public void setQueryCount(int queryCount);
	
	public Query getQuery();
	
	public void setQuery(Query query);
		
	/**
	 * Returns a list of tweets.
	 * 
	 * @param lat		latitude of location where tweets will be extracted.
	 * @param lng		longitude of location where tweets will be extracted.
	 * @param tweetsize	number of tweets to extract.
	 * @param radius	radius around the center giving by lat and lng.
	 * @return			list of tweets
	 */
	public ArrayList<Status> TweetsStream(double lat, double lng, int tweetsize,
			int radius);
	
	/**
	 * Returns a list of tweets.
	 * 
	 * @param event		bandsintown event to track.
	 * @param radius	radius around the center giving by event lat and lng
	 * @return			list of tweets
	 * @throws InterruptedException
	 */
	public ArrayList<Status> StreamConcert(Event event, int radius)
			throws InterruptedException;
	
//	public void run();
}
