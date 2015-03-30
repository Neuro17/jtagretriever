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
		
	public ArrayList<Status> TweetsStream(double lat, double lng, int tweetsize,
			int radius);
	
	public ArrayList<Status> StreamConcert(Event event, int radius)
			throws InterruptedException;
}
