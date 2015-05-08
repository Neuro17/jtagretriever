package dataBaseServiceTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	  HashMap<String,String> map = aService.top(10);
	  Iterator it = map.entrySet().iterator();
      while (it.hasNext()) {
          Map.Entry pair = (Map.Entry)it.next();
          System.out.println(pair.getKey() + " = " + pair.getValue());
          it.remove(); // avoids a ConcurrentModificationException
      }
  }

} 
