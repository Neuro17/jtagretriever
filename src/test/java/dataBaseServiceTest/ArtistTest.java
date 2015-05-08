package dataBaseServiceTest;

import java.util.ArrayList;

import javabandsintown.search.Bandsintown;
import dataBaseService.ArtistService;
import javabandsintown.entity.Artist;

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

//	  artists = aService.findAll();
//	  for(Artist artist : artists)
//		  System.out.println(artist);

//	  aService.deleteAll();
	  
//	  String tag = "shakira";
//	  
//	  System.out.println(aService.checkName(tag) ||
//			  aService.manageTag(tag));

	  ArrayList<String> artistList = aService.top(10);
	  for(String name : artistList)
		  System.out.println(name);
	  
  }

} 
