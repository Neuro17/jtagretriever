package app.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
	    public String galleryPage(ModelAndView modelAndView) {

		  return "gallery";
	  }
	  
	  @RequestMapping("/popular")
	    public String popularPage(ModelAndView modelAndView) {
//TODO	usa le tabelle_searched per dare all utente le foto dei tag più cercati
		  
		  return "popular";
	  }
	  
	  @RequestMapping("/search")
	    public String searchPage(@ModelAttribute("tag") String tag, 
	    		ArrayList<String> urlList, HttpServletRequest request) throws Exception {

System.out.println(">>> Requested search for the tag = " + tag);

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
				  /*	traduce "Pink Floyd" in "PinkFloyd"	*/		  
				  tag = tag.replaceAll("\\s", "");
			  
				  PhotoRetriever pr = new PhotoRetriever();
		        
				  List<MediaFeedData> mediaList = pr.getMediaByTag(tag);
				  
		          for (MediaFeedData mediaFeedData : mediaList) {
		        	  String url = mediaFeedData.getImages().getLowResolution().getImageUrl();
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
