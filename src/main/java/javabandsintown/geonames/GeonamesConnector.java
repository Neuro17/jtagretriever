package javabandsintown.geonames;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javabandsintown.http.HttpConnectorImpl;

public abstract class GeonamesConnector extends HttpConnectorImpl{

	private final Logger log = LogManager.getLogger(GeonamesConnector.class);
	protected Gson gson = new GsonBuilder().setPrettyPrinting().create();
		
	protected URI getUri() {
		return uri;
	}

	protected void setUri(URI uri) {
		this.uri = uri;
	}
	
	protected void buildURI(){
		log.trace("Building URI");
		
		uriBld.setPath(GeonamesConfig.getScheme() + GeonamesConfig.getHost() + GeonamesConfig.getTimeZoneJson());

		try {
			uriBld.build();
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}		
	}
	
	protected void build(){
		try {
			uri = uriBld.build();
		} catch (URISyntaxException e) {
			log.error(e.getMessage());
		}
	}

}