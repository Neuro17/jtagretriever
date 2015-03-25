package app.twitter.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.models.Tweet;
import app.repository.TweetRepository;
import app.tools.Tokenizer;
import app.twitter.TwitterTagExtractor;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import entity.Event;

@Component
public class TwitterTagExtractorImp implements TwitterTagExtractor{
	
	private static final Logger log = LogManager.getLogger(TwitterTagExtractorImp.class);
	
	@Autowired
	TweetRepository tweetRepo;
	
	public TwitterTagExtractorImp(){}
	
	private ArrayList<String> filterTweets(Event e, double radius){
		LatLng center;
		LatLng other;
		double distance;
		
		ArrayList<String> filtered = new ArrayList<String>();
		
		for (Tweet tweet : (List<Tweet>) tweetRepo.findByEventName(e.getTitle())) {
			log.debug(tweet);
			center = new LatLng(e.getVenue().getLatitude(), 
					e.getVenue().getLongitude());
			
			other = new LatLng(tweet.getLat(), tweet.getLng());
			distance = LatLngTool.distance(center, other, LengthUnit.KILOMETER);
			
			if(distance <= radius) {
				filtered.add(tweet.getText());
			}
		}
		
		return filtered;
	}
	
	@Override
	public Map<String, Integer> extracxtTag(Event e, double radius) {
		ArrayList<String> filteredTweets = filterTweets(e, radius);
		Map<String, Integer> rawTag = Tokenizer.tokenize(filteredTweets);
		
//		for (Map.Entry<String, Integer> entry : rawTag.entrySet()) {
//		    log.debug(entry.getKey() + " : " + entry.getValue());
//		}
		
		return rawTag;
	}

}
