package javabandsintown.requests;

import java.util.ArrayList;
import java.util.TimeZone;

import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;
import javabandsintown.entity.Venue;
import javabandsintown.geonames.GeonamesGet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Extractor {
	
	private static final Logger log = LogManager.getLogger(Extractor.class);
	
	@SuppressWarnings("unused")
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public static ArrayList<Event> extractEvents(JsonElement item){
		ArrayList<Event> events = new ArrayList<Event>();
		if(item != null){
			JsonObject eventsAsJson = item.getAsJsonObject();
			
			for(JsonElement e : eventsAsJson.get("resultsPage").getAsJsonArray()){
				events.add(extractEvent(e));
			}
			return events;
		}
		else{ 
			log.trace("No events found");
			return null;
		}
	}
	
	public static ArrayList<Event> extractGMTReferencesEvents(JsonElement item){
		ArrayList<Event> events = new ArrayList<Event>();
		if(item != null){
			JsonObject eventsAsJson = item.getAsJsonObject();
		
			for(JsonElement e : eventsAsJson.get("resultsPage").getAsJsonArray()){
				events.add(extractGMTReferencesEvent(e));
			}
			return events;
		}
		else{ 
			log.trace("No events found");
			return null;
		}
	}
	
	public static Event extractEvent(JsonElement item){
		JsonObject eventsAsJson = item.getAsJsonObject();
		Event event = new Event();
		
		event.setId(eventsAsJson.get("id").getAsInt());
		event.setDatetime(new DateTime(eventsAsJson.get("datetime").getAsString()));
		event.setTitle(eventsAsJson.get("title").getAsString());
		event.setArtist(extractArtists(eventsAsJson.get("artists")));
		event.setVenue(extractVenue(eventsAsJson.get("venue")));
		
		if(!eventsAsJson.get("description").isJsonNull())
			event.setDescription(eventsAsJson.get("description").getAsString());

		return event;
	}
	
	public static Event extractGMTReferencesEvent(JsonElement item){
		JsonObject eventsAsJson = item.getAsJsonObject();
		Event event = new Event();
		GeonamesGet gng = new GeonamesGet();

		TimeZone.getTimeZone("Europe/London");
		int timeToAdd;
		DateTime tmpDateTime;
		
		event.setTitle(eventsAsJson.get("title").getAsString());
		event.setArtist(extractArtists(eventsAsJson.get("artists")));
		event.setId(eventsAsJson.get("id").getAsInt());
		
		event.setVenue(extractVenue(eventsAsJson.get("venue")));
		tmpDateTime = new DateTime(eventsAsJson.get("datetime").getAsString());
				
		if(!eventsAsJson.get("description").isJsonNull())
			event.setDescription(eventsAsJson.get("description").getAsString());
		
		timeToAdd = gng.getHoursToAddToGMT(event.getVenue().getLatitude(), event.getVenue().getLongitude());
		if(timeToAdd == -99)
			event = null;
		else
			event.setDatetime(new DateTime(tmpDateTime.minusHours(timeToAdd)));
		
		return event;
	}
	
	public static Venue extractVenue(JsonElement jsonElement) {
		JsonObject venueAsJson = jsonElement.getAsJsonObject();
		Venue venue = new Venue();
		
		venue.setCity(venueAsJson.get("city").getAsString());
		venue.setCountry(venueAsJson.get("country").getAsString());
		venue.setName(venueAsJson.get("name").getAsString());
		venue.setLatitude(venueAsJson.get("latitude").getAsDouble());
		venue.setLongitude(venueAsJson.get("longitude").getAsDouble());
		
		if(!venueAsJson.get("region").isJsonNull())
			venue.setRegion(venueAsJson.get("region").getAsString());

		return venue;
	}
	
	public static ArrayList<Artist> extractArtists(JsonElement jsonElement) {
		JsonArray artistsAsJson = jsonElement.getAsJsonArray();
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		for(JsonElement a : artistsAsJson)
			artists.add(extractArtist(a));

		return artists;
	}

	public static Artist extractArtist(JsonElement item){
		Artist artist;
		if(item != null){
			JsonObject artistTmp = item.getAsJsonObject();

			if(artistTmp.get("mbid").isJsonNull())
				artist = new Artist(artistTmp.get("name").getAsString());
			else
				artist = new Artist(artistTmp.get("name").getAsString(), artistTmp.get("mbid").getAsString());

			if(!artistTmp.get("thumb_url").isJsonNull())
				artist.setUrlImage(artistTmp.get("thumb_url").getAsString());

			return artist;
		}
		else{
			log.debug("No artist found");
			return null;
		}
	}

	public static ArrayList<Venue> extractVenues(JsonElement item) {
		ArrayList<Venue> venues = new ArrayList<Venue>();
		JsonObject venuesAsJson = item.getAsJsonObject();
		
		for(JsonElement e : venuesAsJson.get("resultsPage").getAsJsonArray()){
			venues.add(extractVenue(e));
		}

		return venues;
	}	
}
