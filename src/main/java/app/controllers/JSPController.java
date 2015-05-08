package app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import dataBaseService.ArtistService;
import dataBaseService.EventService;
import dataBaseService.VenueService;
import app.example.GenericController;
import app.instagram.PhotoRetriever;

@Controller
public class JSPController extends GenericController{

	  @RequestMapping("/jsptest")
	    public String test(ModelAndView modelAndView) {
		  
//		  	String test = "test";
		  
//		  	ModelMap model = new ModelMap();
//	        model.addAttribute("test", "test");
//	        return new ModelAndView("user_list", model);
		  	
//		  	System.out.println(modelAndView);
	    	
	        return "jsp-spring-boot";
	  }

	  @RequestMapping("/home")
	    public String homePage(ModelAndView modelAndView) {

		  return "home";
	  }
	  
	  @RequestMapping("/gallery")
	    public String galleryPage(HttpServletRequest request,
		      HttpServletResponse response) {
		  
		  Cookie[] cookies = request.getCookies();

		  ArrayList<String> lastSearches = new ArrayList<String>();
//	gestione cookies		  
		  if(cookies != null){
			  for(Cookie cookie : cookies)
			          lastSearches.add(cookie.getValue());
//tolgo il primo perchè è un valore casuale
			  lastSearches.remove(0);
//System.out.println(">>>\tCookie red");	  
//for(String search : lastSearches)
//	System.out.println("\t\t" +search);
		  
			  request.setAttribute("list",lastSearches);		  
//	fine gestione cookies		  
		  }
		  return "gallery";
	  }
	  
	  @RequestMapping("/popular")
	    public String popularPage(ArrayList<String> list, 
	    		HttpServletRequest request) throws Exception {
// return a list of top 10 searched object for each entity
//TODO	usa le tabelle_searched per dare all utente le foto dei tag più cercati
		  ArtistService aS = new ArtistService();
		  VenueService vS = new VenueService();
		  EventService eS = new EventService();
		  
		  ArrayList<String> topA = aS.top(5);
		  ArrayList<String> topV = vS.top(5);
		  ArrayList<String> topE = eS.top(5);
		  
		  topA.addAll(topV);
		  topA.addAll(topE);
		  list = topA;

		  request.setAttribute("list",list);		  
		  
		  return "popular";
	  }
	  
	  @RequestMapping("/search")
	  public String searchPage(@ModelAttribute("tag") String tag, 
	      ArrayList<String> urlList, HttpServletRequest request,
	      HttpServletResponse response) throws Exception {

		  ArtistService aS = new ArtistService();
		  VenueService vS = new VenueService();
		  EventService eS = new EventService();
		  
//TODO 	la priorità è dare all utente le immagini
//		prima ricerca su db e su bit e poi salvo valori di ricerca su tabelle_searched
		  
		  boolean validA = false,validV = false,validE = false;
		  
		  validA = aS.checkName(tag) || aS.manageTag(tag);
		  if(!validA)
			  validV = vS.checkName(tag) || vS.manageTag(tag);
		  if(!validV)
			  validE = eS.checkName(tag);

		  if(validA || validV || validE){
//	gestione cookie
		          Cookie cookie  = new Cookie(tag.replaceAll("\\s", ""), tag);
		          response.addCookie(cookie);
//	fine gestione cookie		          

			  	  /*	traduce "Pink Floyd" in "PinkFloyd"	*/		  
				  tag = tag.replaceAll("\\s", "");
			  
				  PhotoRetriever pr = new PhotoRetriever();
		        
				  List<MediaFeedData> mediaList = pr.getMediaByTag(tag);
				  
		          for (MediaFeedData mediaFeedData : mediaList) {
		        	  String url = mediaFeedData.getImages()
		        			  .getLowResolution().getImageUrl();
		        	  urlList.add(url);
		          }          
		          request.setAttribute("urlList",urlList);
				  return "photoAlbum";
			  }
		  else
			  return "unperformed";
	  }

	  @RequestMapping("/unperformed")
	    public String unperformedPage(ModelAndView modelAndView) {

		  return "unperformed";
	  }

}
