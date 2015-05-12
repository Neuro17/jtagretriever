package app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;
import javabandsintown.search.Bandsintown;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.example.GenericController;
import app.instagram.PhotoRetriever;
import app.repository.TweetRepository;
import app.tools.TagExtractor;
import dataBaseService.ArtistService;
import dataBaseService.EventService;
import dataBaseService.VenueService;

@Controller
public class JSPController extends GenericController{
	
	@Autowired
	TweetRepository twitterRepo;
	
	@Autowired
	TagExtractor tagExtractor;
	
	ArtistService aS = new ArtistService();
	VenueService vS = new VenueService();
	EventService eS = new EventService();
	
	Bandsintown bandsintown = new Bandsintown();
	
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
	    public String getHomePage(ModelAndView modelAndView) {

		  return "home";
	  }
	  
	  @RequestMapping("/gallery")
	    public String getGalleryPage(HttpServletRequest request,
		      HttpServletResponse response) throws Exception {
		  
		  Cookie[] cookies = request.getCookies();
		  HashMap<String,String> map = new HashMap<String,String>();
		  ArrayList<String> artistsList = new ArrayList<String>();
		  
//		gestione cookies		  
		  if(cookies != null){
			  for(Cookie cookie : cookies){
			      String name = cookie.getValue();
			      artistsList.add(name);
			  }
		  for(String name : artistsList){
			  Artist aTmp = aS.findById(name);
			  if(aTmp != null)
				  map.put(name, aTmp.getUrlImage());
		  }
//		fine gestione cookies		  
			  request.setAttribute("map",map);		  
		  }
		  return "gallery";
	  }
	  
	  @RequestMapping("/popular")
	    public String getPopularPage(HashMap<String,String> map,
	    		ArrayList<String> list, HttpServletRequest request) throws Exception {
//TODO impostare periodicamente l aggionamento dell urlImage per ogni artista nel db

//		  VenueService vS = new VenueService();
//		  EventService eS = new EventService();
//TODO 	decidere se in popular inserire solo artisti o anche le altre entità

		  HashMap<String,String> topA = aS.top(8);
//		  ArrayList<String> topV = vS.top(8);
//		  ArrayList<String> topE = eS.top(8);
		  
		  map = topA;
		  
//		  topV.addAll(topE);
//		  list = topV;

		  request.setAttribute("map", map);
		  request.setAttribute("list",list);		  
		  
		  return "popular";
	  }
	  
	  @RequestMapping("/search")
	  public String getSearchPage(@ModelAttribute("tag") String tag, 
	      ArrayList<String> urlList, HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
		//TODO copia le modifiche di javabandsintown su github
  
		//TODO	dopo ricerca di un artista cerca gli eventi dell artista e li propone 
//				come menu tendina
				
		//TODO	20 foto
		  
//		  TODO - aggiungere a photoretriever funzione con location data e tag
		  tag = WordUtils.capitalizeFully(tag);
		  
		  boolean validA = false,validV = false,validE = false;
		  
		  validA = aS.checkName(tag) || aS.manageTag(tag);
		  if(!validA)
			  validV = vS.checkName(tag) || vS.manageTag(tag);
		  if(!validV)
			  validE = eS.checkName(tag);

		  if(validA || validV || validE){
//	gestione cookie
			  if(validA){
				  Cookie cookie;
			      cookie  = new Cookie(tag.replaceAll("\\s", ""), tag);
			      response.addCookie(cookie);
			  }
//	fine gestione cookie		          

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
	  
	  /**
	   * Display concerts list given an artistName from home page
	   * 
	 * @param artistName
	 * @param urlList
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/search-test")
	  public String getSearchPageTest(@ModelAttribute("tag") String artistName, 
	      ArrayList<String> urlList, HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
		  ArrayList<Event> events = new ArrayList<Event>();
		  ArrayList<String> tag = new ArrayList<String>();
		  
		  if (aS.checkName(artistName) || aS.manageTag(artistName)) {
		  	events = bandsintown.getEvents.setArtist(artistName).setDate("all").search();
		  	
		  }
		  
		  tag = tagExtractor.extractTagFromBandsintown(events.get(0));
		  
		  PhotoRetriever pr = new PhotoRetriever();
	      System.out.println(tag);
		  List<MediaFeedData> mediaList = pr.getMediaByTagList(tag);
		  
          for (MediaFeedData mediaFeedData : mediaList) {
        	  String url = mediaFeedData.getImages()
        			  .getLowResolution().getImageUrl();
        	  urlList.add(url);
          }          
          request.setAttribute("urlList",urlList);
		  return "photoAlbum";
		  
	  }
	  
	
	  /**
	   * Shows a list of photos given an eventName from search-test page
	   * 
	 * @param eventName
	 * @param urlList
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/concert-test")
	  public String getConcertTest(@ModelAttribute("tag") String eventName, 
	      ArrayList<String> urlList, HttpServletRequest request,
	      HttpServletResponse response) throws Exception {
		  ArrayList<String> tag = new ArrayList<String>();
		  
		  if(twitterRepo.findByEventName(eventName) != null){
			  
		  }
		  
		  return null;
	  }

	  @RequestMapping("/unperformed")
	    public String getUnperformedPage(ModelAndView modelAndView) {

		  return "unperformed";
	  }

}
