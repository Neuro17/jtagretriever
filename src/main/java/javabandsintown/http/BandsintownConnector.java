package javabandsintown.http;

import java.net.URISyntaxException;

import javabandsintown.config.BandsintownConfig;
import javabandsintown.requests.Parameters;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public abstract class BandsintownConnector extends HttpConnectorImpl {
	private static final Logger log = LogManager.getLogger(BandsintownConnector.class);
	protected Gson gson = new GsonBuilder().setPrettyPrinting().create();

	protected Integer page;
	protected int pages;
	
	protected void buildURI(){
		log.trace("Building URI");
		
		uriBld.setScheme(BandsintownConfig.getScheme()).setHost(BandsintownConfig.getHost());
		
		try {
			log.debug("Built base url: " + uriBld.build());
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
		
		log.trace("Succesfully built URI"); 
	}
	
	protected BandsintownConnector setArtistID(String id) {
		log.trace("Entering setArtistID");
		uriBld.setParameter(Parameters.getArtistId(), id);
		log.trace("Exiting setArtistID");
		return this;
	}
	
	protected BandsintownConnector asJson(){
		log.trace("Entering asJson");
		uriBld.setParameter(Parameters.getFormat(), "json");
		log.trace("Exiting asJson");
		return this;
	}
	
	protected BandsintownConnector asXML(){
		log.trace("Entering asXML");
		uriBld.setParameter(Parameters.getFormat(), "XML");
		log.trace("Exiting asXML");
		return this;
	}
	
	private BandsintownConnector setAppId() {
		log.trace("Entering setAppId");
		uriBld.setParameter(Parameters.getAppId(), BandsintownConfig.getApiKey());
		log.trace("Exiting setAppId");
		return this;
	}
	
	private BandsintownConnector setApiVersion(){
		log.trace("Entering setApiVersion");
		uriBld.setParameter(Parameters.getApiVersion(), BandsintownConfig.getApiVersion());
		log.trace("Exiting setApiVersion");
		return this;
	}
	
	protected void build(){
		log.trace("Entering build");
		setAppId();
		setApiVersion();
		try {
			uri = uriBld.build();
			log.debug(uri);
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
		log.trace("Exiting build");
	}
}
