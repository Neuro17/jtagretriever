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
		uriBld.setScheme(BandsintownConfig.getScheme()).setHost(BandsintownConfig.getHost());
		
		try {
			uriBld.build();
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}		
	}
	
	protected BandsintownConnector setArtistID(String id) {
		uriBld.setParameter(Parameters.getArtistId(), id);

		return this;
	}
	
	protected BandsintownConnector asJson(){
		uriBld.setParameter(Parameters.getFormat(), "json");

		return this;
	}
	
	protected BandsintownConnector asXML(){
		uriBld.setParameter(Parameters.getFormat(), "XML");

		return this;
	}
	
	private BandsintownConnector setAppId() {
		uriBld.setParameter(Parameters.getAppId(), BandsintownConfig.getApiKey());

		return this;
	}
	
	private BandsintownConnector setApiVersion(){
		uriBld.setParameter(Parameters.getApiVersion(), BandsintownConfig.getApiVersion());

		return this;
	}
	
	protected void build(){
		setAppId();
		setApiVersion();
		
		try {
			uri = uriBld.build();
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
	}
}
