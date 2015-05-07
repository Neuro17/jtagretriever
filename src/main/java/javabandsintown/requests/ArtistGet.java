package javabandsintown.requests;


import javabandsintown.config.BandsintownConfig;
import javabandsintown.entity.Artist;
import javabandsintown.http.BandsintownConnector;

import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

public class ArtistGet extends BandsintownConnector{
	
	private static final Logger log = LogManager.getLogger(ArtistGet.class);
	
	public ArtistGet(){
		uriBld = new URIBuilder();
		buildURI();
		uriBld.setPath(BandsintownConfig.getArtistPath());
	}
	

	public ArtistGet setArtist(String name){
		log.trace("Entering setArtist");
		
		uriBld.setPath(BandsintownConfig.getArtistPath() + "/" + name);
		
		log.trace("Exiting setArtist");
		return this;
	}
	
	public ArtistGet asJson(){
		return (ArtistGet) super.asJson();
	}
	
	public ArtistGet asXML(){
		return (ArtistGet) super.asXML();
	}
	
	public ArtistGet setArtistID(String id){
		return (ArtistGet) super.setArtistID(id);
	}
	
	public Artist search(){
		log.trace("Entering search");
		JsonObject artistAsJson;
		
		build();
		log.debug(uri);
		
		artistAsJson = executeRequest();
		log.debug(artistAsJson);
		
		log.trace("Exiting search");
		return Extractor.extractArtist(artistAsJson);
	}
}
