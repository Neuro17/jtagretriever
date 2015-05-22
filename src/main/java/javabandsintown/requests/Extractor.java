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
//		log.trace("Entering extractEvents");
		ArrayList<Event> events = new ArrayList<Event>();
		if(item != null){
			JsonObject eventsAsJson = item.getAsJsonObject();
			
//			log.debug(item);
//			log.debug(eventsAsJson);
			
			for(JsonElement e : eventsAsJson.get("resultsPage").getAsJsonArray()){
				events.add(extractEvent(e));
			}
//			log.trace("Exiting extractEvents");
			return events;
		}
		else{ 
			log.trace("No events found");
			return null;
		}
	}
	
	public static ArrayList<Event> extractGMTReferencesEvents(JsonElement item){
//		log.trace("Entering extractEvents");
		ArrayList<Event> events = new ArrayList<Event>();
		if(item != null){
			JsonObject eventsAsJson = item.getAsJsonObject();
		
//		log.debug(item);
//		log.debug(eventsAsJson);
		
			for(JsonElement e : eventsAsJson.get("resultsPage").getAsJsonArray()){
				events.add(extractGMTReferencesEvent(e));
			}
	//		log.trace("Exiting extractEvents");
			return events;
		}
		else{ 
			log.trace("No events found");
			return null;
		}
	}
	
	public static Event extractEvent(JsonElement item){
//		log.trace("Entering extractEvent");
		JsonObject eventsAsJson = item.getAsJsonObject();
		Event event = new Event();
		
		event.setId(eventsAsJson.get("id").getAsInt());
		event.setDatetime(new DateTime(eventsAsJson.get("datetime").getAsString()));
		event.setTitle(eventsAsJson.get("title").getAsString());
		event.setArtist(extractArtists(eventsAsJson.get("artists")));
		event.setVenue(extractVenue(eventsAsJson.get("venue")));
		
		if(!eventsAsJson.get("description").isJsonNull())
			event.setDescription(eventsAsJson.get("description").getAsString());
//		log.debug("Event found : " + event);
//		log.trace("Exiting extractEvent");
		return event;
	}
	
	public static Event extractGMTReferencesEvent(JsonElement item){
//		log.trace("Entering extractGMTReferencesEvent");
		
		JsonObject eventsAsJson = item.getAsJsonObject();
		Event event = new Event();
		GeonamesGet gng = new GeonamesGet();
//setting timeZoneReferences to Europe/London
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
//sottraggo le ore da aggiungere al GMT		
			event.setDatetime(new DateTime(tmpDateTime.minusHours(timeToAdd)));
		
//		log.debug("Event found : " + event);		
//		log.trace("Exiting extractGMTReferencesEvent");
		return event;
	}
	
	public static Venue extractVenue(JsonElement jsonElement) {
//		log.trace("Entering extractVenue");
		JsonObject venueAsJson = jsonElement.getAsJsonObject();
		Venue venue = new Venue();
		
//		log.debug(gson.toJson(venueAsJson));
		
		venue.setCity(venueAsJson.get("city").getAsString());
		venue.setCountry(venueAsJson.get("country").getAsString());
		venue.setName(venueAsJson.get("name").getAsString());
		venue.setLatitude(venueAsJson.get("latitude").getAsDouble());
		venue.setLongitude(venueAsJson.get("longitude").getAsDouble());
		
		if(!venueAsJson.get("region").isJsonNull())
			venue.setRegion(venueAsJson.get("region").getAsString());

//		log.debug("Venue found : " + venue);

//		log.trace("Exiting extractVenue");
		return venue;
	}
	
	public static ArrayList<Artist> extractArtists(JsonElement jsonElement) {
//		log.trace("Entering extractArtists");
		JsonArray artistsAsJson = jsonElement.getAsJsonArray();
		ArrayList<Artist> artists = new ArrayList<Artist>();
		
		for(JsonElement a : artistsAsJson)
			artists.add(extractArtist(a));

//		log.trace("Exiting extractArtists");
		return artists;
	}

	public static Artist extractArtist(JsonElement item){
//		log.trace("Entering extractArtist");
		Artist artist;
		if(item != null){
			JsonObject artistTmp = item.getAsJsonObject();
//			log.debug(artistTmp);
			if(artistTmp.get("mbid").isJsonNull())
				artist = new Artist(artistTmp.get("name").getAsString());
			else
				artist = new Artist(artistTmp.get("name").getAsString(), artistTmp.get("mbid").getAsString());

			if(!artistTmp.get("thumb_url").isJsonNull())
				artist.setUrlImage(artistTmp.get("thumb_url").getAsString());

//			log.debug("Artist found : " + artist);
//			log.trace("Exiting extractArtist");
			return artist;
		}
		else{
			log.debug("No artist found");
			return null;
		}
	}

	public static ArrayList<Venue> extractVenues(JsonElement item) {
//		log.trace("Entering extractEvents");
		ArrayList<Venue> venues = new ArrayList<Venue>();
		JsonObject venuesAsJson = item.getAsJsonObject();
		
//		log.debug(item);
//		log.debug(venuesAsJson);
		
		for(JsonElement e : venuesAsJson.get("resultsPage").getAsJsonArray()){
			venues.add(extractVenue(e));
		}

//		log.trace("Exiting extractEvents");
		return venues;
	}
	
	
}
