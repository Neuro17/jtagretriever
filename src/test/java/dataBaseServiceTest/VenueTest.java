package dataBaseServiceTest;

import java.util.ArrayList;

import javabandsintown.search.Bandsintown;
import dataBaseService.ArtistService;
import dataBaseService.VenueService;
import javabandsintown.entity.Venue;

public class VenueTest{
	
  public static void main(String[] args) throws Exception {
	  
	  VenueService vService = new VenueService();
	  Venue v = new Venue();
	  ArrayList<Venue> venues =new ArrayList<Venue>();

//	  v.setId(4);
//	  v.setCity("d");
//	  v.setCountry("d");
//	  v.setLatitude(45);
//	  v.setLongitude(54);
//	  v.setRegion("d");
//	  v.setName("d");
//
//	  vService.persist(v);

//	  System.out.println(vService.checkName("Forum"));

//	  v.setId(2);
//	  v.setCity("updatedCity");
//	  v.setCountry("updateCountry");
//	  v.setName("updatedName");
//	  v.setLatitude(99);
//	  v.setLongitude(99);
//	  v.setRegion("updatedRegion");
//	  v.setName("updatedName");
//
//	  vService.update(v);

//	  int id = 3;
//	  vService.delete(id);

//	  venues = vService.findAll();
//	  for(Venue venue : venues)
//		  System.out.println(venue);

//	  vService.deleteAll();

//	  venues = vService.findByName("The Forum");
	  
//	  for(Venue ve : venues)
//		  System.out.println(ve.getName() + " " + ve.getLatitude() + " "
//		  		+ ve.getLongitude());

	  String tag = "Estadio GEBA";
	  
	  System.out.println(vService.checkName(tag)
			  || vService.manageTag(tag));
	  
  }

} 
