package app.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javabandsintown.entity.Artist;
import javabandsintown.entity.Event;
import javabandsintown.http.BandsintownConnector;
import javabandsintown.search.Bandsintown;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
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

	private static final Logger log = LogManager.getLogger(JSPController.class);

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

		  HashMap<String,String> topA = aS.top(12);
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

		  tag = WordUtils.capitalizeFully(tag);
		  
		  boolean validA = false,validV = false,validE = false;
		  
		  validA = aS.checkName(tag) || aS.manageTag(tag);
		  if(!validA)
			  validV = vS.checkName(tag) || vS.manageTag(tag);
		  if(!validV)
			  validE = eS.checkName(tag);
		  if(validA || validV || validE){
//	gestione cookie solo relativamente ad artisti
			  if(validA){
				  Cookie cookie;
			      cookie  = new Cookie(tag.replaceAll("\\s", ""), tag);
			      response.addCookie(cookie);
			  }
//	fine gestione cookie		       
			  
			  if(validA){
				  String artistName = tag;
log.trace("processing artist events");
				  ArrayList<Event> events = bandsintown.getEvents.setArtist(artistName).setDate("all").search();
log.trace("events found " + events.size());
				for(Event e : events)
					eS.persist(e);

				  request.setAttribute("eventList", events);

				  return "artist-events";  	
			  }
			  
log.trace("searching for Venue or Event");			  
			  tag = tag.replaceAll("\\s", "");
			  
			  PhotoRetriever pr = new PhotoRetriever();
	        
			  List<MediaFeedData> mediaList = pr.getMediaByTag(tag,100);
			  
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

	  @RequestMapping("/artist-home")
	  public String getSearchPageTest(@ModelAttribute("tag") String artistName, 
			  HttpServletRequest request,HttpServletResponse response,
			  ArrayList<Event> events) throws Exception {
log.trace("requested artist-home");
//		  ArrayList<Event> events = new ArrayList<Event>();
		  
		  events = bandsintown.getEvents.setArtist(artistName).setDate("upcoming").search();
log.trace("found " + events.size() + " for artist " + artistName);
		  request.setAttribute("eventList", events);

		  return "artist-event-home";
	  }
	
		@RequestMapping("/artist-event-home")
		public String getArtistEventHome(@ModelAttribute("eventId") int eventId,
				HttpServletRequest request, HttpServletResponse response,
			  ArrayList<String> urlList) throws Exception{
log.trace("eventId requested " + eventId);
			Event event = eS.findById(eventId);
log.trace("event found " + event);
			ArrayList<String> tags = new ArrayList<String>();

			tags = tagExtractor.extractTagFromBandsintown(event);
		  
			PhotoRetriever pr = new PhotoRetriever();

			List<MediaFeedData> mediaList = pr.getMediaByTagList(tags);

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
		  ArrayList<String> tags = new ArrayList<String>();
		  
		  if(twitterRepo.findByEventName(eventName) != null){
			  
		  }
		  
		  return null;
	  }

	  @RequestMapping("/unperformed")
	    public String getUnperformedPage(ModelAndView modelAndView) {

		  return "unperformed";
	  }

}
