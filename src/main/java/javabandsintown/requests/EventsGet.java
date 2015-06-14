package javabandsintown.requests;

import java.util.ArrayList;

import javabandsintown.config.BandsintownConfig;
import javabandsintown.entity.Event;
import javabandsintown.http.BandsintownConnector;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

public class EventsGet extends BandsintownConnector{
	
	private static final Logger log = LogManager.getLogger(EventsGet.class);

	public EventsGet(){
		uriBld = new URIBuilder();
		buildURI();
		uriBld.setPath(BandsintownConfig.getArtistPath());
	}
	
	public EventsGet setArtist(String name){
		uriBld.setPath(BandsintownConfig.getArtistPath() + "/" + name + "/events");

		return this;
	}
	
	public EventsGet setDate(String date){
		uriBld.setParameter(Parameters.getDate(), date);

		return this;
	}
	
	public EventsGet asJson(){
		return (EventsGet) super.asJson();
	}
	
	public EventsGet asXML(){
		return (EventsGet) super.asXML();
	}
	
	public EventsGet setArtistID(String id){
		return (EventsGet) super.setArtistID(id);
	}

	public ArrayList<Event> search() {
		JsonObject events;
		
		build();
		events = executeRequest();
		log.debug(events);

		return Extractor.extractEvents(events);
		
	}

	public ArrayList<Event> searchGMTReferences() {
		JsonObject events;
		
		build();
		events = executeRequest();

		return Extractor.extractGMTReferencesEvents(events);
		
	}
}
