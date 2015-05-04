package app.tools;

import java.util.ArrayList;
import java.util.Map;

import entity.Event;

public interface TagExtractor {
	
	/**
	 * Given a bandsintown Event and a radius extract all the tags found in the 
	 * tweets extracted within the radius from center of the Event, and their count.
	 * 
	 * @param e	bandsintown event
	 * @param radius
	 * @return Map<String, Integer> where String represent the tag 
	 * 			and Integer is its count among tweets
	 */
	public Map<String, Integer> extractTagFromTweets(Event e, double radius);
	
	/**
	 * @param event	bandsintown event
	 * @return
	 */
	public ArrayList<String> extractTagFromBandsintown(Event event);
	
	/**
	 * @param e
	 * @param radius
	 * @return
	 */
	public ArrayList<String> extractTag(Event e, double radius);

	
}
