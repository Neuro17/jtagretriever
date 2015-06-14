package javabandsintown.search;

import javabandsintown.requests.ArtistGet;
import javabandsintown.requests.EventsGet;
import javabandsintown.requests.VenueEventsSearch;

public class Bandsintown {
	
	public ArtistGet getArtist;
	public EventsGet getEvents;
	public VenueEventsSearch getVenues;
	
	public Bandsintown() {
		getArtist = new ArtistGet();
		getEvents = new EventsGet();
		getVenues = new VenueEventsSearch();
	}
}
