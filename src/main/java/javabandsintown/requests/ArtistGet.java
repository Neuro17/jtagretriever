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
		uriBld.setPath(BandsintownConfig.getArtistPath() + "/" + name);
		
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
		JsonObject artistAsJson;
		
		build();
		
		artistAsJson = executeRequest();

		return Extractor.extractArtist(artistAsJson);
	}
}
