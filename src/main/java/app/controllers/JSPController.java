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
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.example.GenericController;
import app.instagram.PhotoRetriever;
import app.repository.TweetRepository;
import app.tools.TagExtractor;
import app.tools.Task;
import app.tools.Tools;
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
	  public String getSearchPage(@ModelAttribute("tag") String tag, ArrayList<String> urlList, HttpServletRequest request, HttpServletResponse response) throws Exception {  
		  Artist artistTmp = null;
		  ArrayList<Event> events = null;
		  List<Event> eventsToArtistEventsPage = new ArrayList<Event>();
		  
		  tag = WordUtils.capitalizeFully(tag);
		  
		  boolean validA = false, validV = false, validE = false;
		  
		  validA = aS.checkName(tag) || aS.manageTag(tag);
log.debug(validA);
		  if(!validA){
			  return "empty-events";
		  }
		 
		  Cookie cookie;
		  cookie  = new Cookie(tag.replaceAll("\\s", ""), tag);
		  response.addCookie(cookie);

		  DateTime now = DateTime.now();
		  		  
		  artistTmp = aS.findById(tag);
		  events = aS.getAllEvents(artistTmp.getName());
		  Task.addArtistToFile(artistTmp.getName());
log.debug("total events found " + events.size());
			
		  for(Event e : events){
			  if(e.getDatetime().isAfter(now.minusMonths(6)) &&
					  e.getDatetime().isBefore(now.plusDays(1))){
				  eventsToArtistEventsPage.add(e);
			  }
		  }
log.trace("events to page " + eventsToArtistEventsPage.size());
		  
		  if(events.size() == 0 || eventsToArtistEventsPage.size() == 0) {
			  return "empty-events";
		  }

		  request.setAttribute("eventList", eventsToArtistEventsPage);
//	returns a page with a list of events each linked with ?eventId=XXXXXX
//	after clicking on one of these, you will be redirect to artist-event-home
//	that manage to find the tag and then returns to photoAlbum
		  return "artist-events";  
	  }

	  @RequestMapping("/artist-home")
	  public String getSearchPageTest(@ModelAttribute("tag") String artistName, 
			  HttpServletRequest request,HttpServletResponse response,
			  ArrayList<Event> events) throws Exception {
		  
//		  ArrayList<Event> events = new ArrayList<Event>();

		  events = bandsintown.getEvents.setArtist(artistName).setDate("all").search();

		  request.setAttribute("eventList", events);

		  return "artist-event-home";
	  }
	
		@RequestMapping("/artist-event-home")
		public String getArtistEventHome(@ModelAttribute("eventId") int eventId,
				HttpServletRequest request, HttpServletResponse response,
			  ArrayList<String> urlList) throws Exception{
//
			Event event = eS.findById(eventId);
log.trace(event);
			ArrayList<String> tags = new ArrayList<String>();
log.trace("extracting tags from bandsintown");
			tags = tagExtractor.extractTagFromBandsintown(event);
log.trace("tags for event " + tags);

			PhotoRetriever pr = new PhotoRetriever();
						
//			String artistName = event.getArtist().get(0).getName().replaceAll("\\s", "");
			
			List<MediaFeedData> mediaList = pr.getMedia3(tags, 
					event.getVenue().getLatitude(), event.getVenue().getLongitude(),
					event.getDatetime().minusHours(6), event.getDatetime().plusHours(18),
					5000L, 25);

			for (MediaFeedData mediaFeedData : mediaList) {
//log.debug((new DateTime(Long.parseLong(mediaFeedData.getCreatedTime())*1000)).toString());
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

	@RequestMapping("/emptyEvents")
	public String getEmptyEventsPage(ModelAndView modelAndView) {

		return "empty-events";
	}
	
	@RequestMapping("/unperformed")
	public String getUnperformedPage(ModelAndView modelAndView) {

		return "unperformed";
	}

}
