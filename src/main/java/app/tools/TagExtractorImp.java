package app.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import app.models.Tweet;
import app.repository.TweetRepository;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;

@Component
public class TagExtractorImp implements TagExtractor{
	
	private static final Logger log = LogManager.getLogger(TagExtractorImp.class);
	
	@Autowired
	TweetRepository tweetRepo;
	
	public TagExtractorImp(){}
	
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
	public Map<String, Integer> extractTagFromTweets(Event e, double radius) {
		ArrayList<String> filteredTweets = filterTweets(e, radius);
		Map<String, Integer> rawTag = Tokenizer.tokenize(filteredTweets);
		
		return rawTag;
	}

	
	@Override
	public ArrayList<String> extractTagFromBandsintown(Event event) {
		ArrayList<String> tag = new ArrayList<String>();
				
		for(Artist artist : event.getArtist()) {
			tag.add(artist.getName().toLowerCase().replaceAll("\\s","")
					.replaceAll("!","").replaceAll("-","")
					.replaceAll("\\(", "").replaceAll("\\)", "")
					.replaceAll("\\.", "").replaceAll("\\&", "and")
					.replaceAll("\\'","").replaceAll("\\+","")
					.replaceAll("\\$","s"));
		}
		
		tag.addAll(extractMultipleTags(event.getVenue().getCity()));
		
		tag.addAll(extractMultipleTags(event.getVenue().getCountry()));

		tag.addAll(extractMultipleTags(event.getVenue().getName()));

		return tag;
	}
	
	private ArrayList<String> extractMultipleTags(String name) {
		ArrayList<String> tags = new ArrayList<String>();

		tags.add(name.toLowerCase().replaceAll("\\s","").replaceAll("!","")
				.replaceAll("-","").replaceAll("\\(", "")
				.replaceAll("\\)", "").replaceAll("'", "")
				.replaceAll("\\.", ""));
		
		if(name.contains(" ")){
			String[] singleWords = name.toLowerCase().split(" ");
			
			for(String sW : singleWords)
				tags.add(sW.replaceAll("!","").replaceAll("-","")
						.replaceAll("\\(", "").replaceAll("\\)", "")
						.replaceAll("'", "").replaceAll("\\.", ""));
		}
		return tags;
	}

	public ArrayList<String> extractTag(Event e, double radius) {
		ArrayList<String> tag = new ArrayList<String>();
		
		tag.addAll(extractTagFromBandsintown(e));
		
		if(tweetRepo.count() > 0) {
			Map<String, Integer> rawTag = extractTagFromTweets(e, radius);
			
			int occurrency = 3;
			
			for(Map.Entry<String, Integer> entry : rawTag.entrySet()){
				if(entry.getValue() >= occurrency) {
					tag.add(entry.getKey());
				}
			}
		}
				
		return tag;
	}
	
//	public static void main(String[] args) throws Exception{
//		EventService es = new EventService();
//		Event e = es.findById(9069374);
//		Bandsintown bandsintown = new Bandsintown();
//		Event event = bandsintown.getEvents.setArtist("foo fighters").setDate("upcoming").search().get(0);
//		TagExtractorImp t = new TagExtractorImp();
////		for(String tag : t.extractTagFromBandsintown(event)){
////			log.debug(tag);
////		};
//		t.extractTag(e, 1);
//	}

}
