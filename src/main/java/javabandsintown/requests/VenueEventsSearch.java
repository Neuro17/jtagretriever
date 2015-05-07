package javabandsintown.requests;

import javabandsintown.http.BandsintownConnector;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonObject;

import javabandsintown.config.BandsintownConfig;
import javabandsintown.entity.Venue;

/**
 * Returns venues matching a search query (supports location filtering).
 * @author biagio
 *
 */
public class VenueEventsSearch extends BandsintownConnector {
	
	private static final Logger log = LogManager.getLogger(VenueEventsSearch.class);
	private URIBuilder lastUriBld;
	
	public VenueEventsSearch(){
		uriBld = new URIBuilder();
		buildURI();
		uriBld.setPath(BandsintownConfig.getVenuePath() + "/search");
	}
	
	/**
	 * @param query - String representing the venue. It works with city name too.
	 * @return VenueEventsSearch
	 */
	public VenueEventsSearch query(String query){
		log.trace("Entering query");
		uriBld.setParameter(Parameters.getQuery(), query);
		log.trace("Exiting query");
		return this;
	}
	
	public VenueEventsSearch asJson(){
		return (VenueEventsSearch) super.asJson();
	}
	
	public VenueEventsSearch asXML(){
		return (VenueEventsSearch) super.asXML();
	}
	
	/**
	 * Set number of results per page. Default is 5.
	 * @param perPage
	 * @return
	 */
	public VenueEventsSearch setPerPage(Integer perPage){
		Integer max = 100;
		
		if(perPage > 100)
			log.warn("Max results per page are 100");
		else
			max = perPage;
		
		uriBld.setParameter(Parameters.getPerPage(), max.toString());
		return this;
	}
	
	public VenueEventsSearch setPage(Integer page){
		uriBld.setParameter(Parameters.getPage(), page.toString());
		return this;
	}
	
	
	public ArrayList<Venue> search() {
		//TODO - torna anche le venue che hanno nel nome la parola cercata e non solo se sono in quella città
		log.trace("Entering search");
		JsonObject venues;
		
		BandsintownConfig.setApiVersion("1.0");
		
		build();
		
		lastUriBld = new URIBuilder(uri);
		
		venues = executeRequest();
		
		log.debug(venues);
		log.trace("Exiting search");
		
		BandsintownConfig.setApiVersion("2.0");
		
		return Extractor.extractVenues(venues);
		
	}
	
	public ArrayList<Venue> nextPage(){
		boolean hasPageParam = false;
		uriBld = lastUriBld;
		
		for(NameValuePair s : lastUriBld.getQueryParams()){
			if(s.getName().equals(Parameters.getPage())){
				log.debug("trovato");
				hasPageParam = true;
				setPage(new Integer(s.getValue()) + 1);
			}
		}
		
		if(!hasPageParam)
			setPage(2);

		return search();
	}
}
