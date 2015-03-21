package dataBaseServiceTest;

import java.util.ArrayList;

import org.joda.time.DateTime;

import dataBaseService.EventService;
import dataBaseService.VenueService;
import entity.Artist;
import entity.Event;
import entity.Venue;

public class EventTest{
	
  public static void main(String[] args) throws Exception {

	  EventService eService = new EventService();
	  VenueService vService = new VenueService();
	  Event e = new Event();
	  ArrayList<Event> events =new ArrayList<Event>();
	  Venue v = new Venue();
	  ArrayList<Artist> eArtist = new ArrayList<Artist>();

//	  e = eService.findById(3);
//	  System.out.println("event : " + e.getId());
//	  eArtist = e.getArtist();
//	  if(eArtist != null){
//		  System.out.println("with artists : ");
//		  for(Artist a : eArtist)
//			  System.out.println(a);
//	  }
	  
//	  v.setId(2);
//
//	  e.setId(1);
//	  e.setTitle("c");
//	  e.setVenue(v);
//	  e.setDescription("c");
//	  e.setDatetime(new DateTime());
//
//	  eArtist.add(new Artist("philip"));
//	  eArtist.add(new Artist("ukulele"));
//	  eArtist.add(new Artist("nobraino"));
//	  e.setArtist(eArtist);
//
//	  eService.persist(e);
	  
	  events = eService.findAll();

	  for(Event event : events){
		  System.out.println("\nEvent \t" + event.getId() + "\t" + event.getTitle() + "" +
			  				"\t" + event.getDescription() + "\t"+ event.getDatetime()+"" +
			  						"\tin Venue " + event.getVenue().getId() + " with Artists: ");
		  ArrayList<Artist> artists = event.getArtist();
		  for(Artist artist : artists)
			  System.out.println(artist);
	  }

//	  v.setId(1);
//	  
//	  e.setId(1);
//	  e.setTitle("updatedTitle");
//	  e.setDatetime(new DateTime());
//	  e.setDescription("updatedDescription");
//	  e.setVenue(v);
//
//	  eArtist.add(new Artist("philip"));
//	  eArtist.add(new Artist("nobraino"));
//	  e.setArtist(eArtist);
//
//	  eService.update(e);

//	  int id = 1;
//	  eService.delete(id);

//	  eService.deleteAll();
	  
  }

} 
