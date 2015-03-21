package dataBaseServiceTest;

import java.util.ArrayList;

import dataBaseService.ArtistService;
import entity.Artist;

public class ArtistTest{
	
  public static void main(String[] args) throws Exception {

	  ArtistService aService = new ArtistService();
	  Artist a = new Artist();
	  ArrayList<Artist> artists =new ArrayList<Artist>();

	  //ARTIST SERVICE VERIFIED
	  
//	  a.setName("bruno mars"); a.setId("6");
//	  aService.persist(a);

//	  a = aService.findById("nobraino");
//	  System.out.println(a);

//	  a.setName("ukulele"); a.setId("100");
//	  aService.update(a);

//	  String name = "madonna";
//	  aService.delete(name);

	  artists = aService.findAll();
	  for(Artist artist : artists)
		  System.out.println(artist);

//	  aService.deleteAll();
	  
  }

} 
