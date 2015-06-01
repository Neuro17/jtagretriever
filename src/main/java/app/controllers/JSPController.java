package app.controllers;

import java.util.ArrayList;
import java.util.Collections;
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
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import app.example.GenericController;
import app.instagram.PhotoRetriever;
import app.repository.TweetRepository;
import app.tools.CustomComparator;
import app.tools.TagExtractor;
import app.tools.Task;
import app.tools.Tools;
import dataBaseService.ArtistService;
import dataBaseService.EventService;
import dataBaseService.Photo;
import dataBaseService.PhotoService;
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
log.trace("total artists to page " + map.size());
		  return "gallery";
	  }
	  
	  @RequestMapping("/popular")
	    public String getPopularPage(HashMap<String,String> map,
	    		ArrayList<String> list, HttpServletRequest request) throws Exception {
//TODO impostare periodicamente l aggionamento dell urlImage per ogni artista nel db

//		  VenueService vS = new VenueService();
//		  EventService eS = new EventService();

		  HashMap<String,String> topA = aS.top(16);
//		  ArrayList<String> topV = vS.top(8);
//		  ArrayList<String> topE = eS.top(8);
		  
		  map = topA;
		  
//		  topV.addAll(topE);
//		  list = topV;

		  request.setAttribute("map", map);
		  request.setAttribute("list",list);		  
log.trace("popular artists to page " + map.size());
		  return "popular";
	  }
	  
	  @RequestMapping("/search")
	  public String getSearchPage(@ModelAttribute("tag") String tag, ArrayList<String> urlList, HttpServletRequest request, HttpServletResponse response) throws Exception {  
		  Artist artistTmp = null;
		  ArrayList<Event> events = null;
		  List<Event> eventsToArtistEventsPage = new ArrayList<Event>();
		  
		  tag = WordUtils.capitalizeFully(tag);

		  boolean validA = false, validV = false, validE = false;
		  
		  validA = aS.checkName(tag) || 
				  aS.manageTag(tag);
//log.debug(validA);
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
		  
		  events = bandsintown.getEvents.setArtist(artistName).setDate("all").search();

		  request.setAttribute("eventList", events);

		  return "artist-event-home";
	  }
	
		@RequestMapping("/artist-event-home")
		public String getArtistEventHome(@ModelAttribute("eventId") int eventId,
				HttpServletRequest request, HttpServletResponse response,
			  ArrayList<String> urlList) throws Exception{

			Event event = eS.findById(eventId);
log.trace("processing " + event);			
			ArrayList<String> tags = tagExtractor.extractTag(event,5000L);
log.trace("tags retrieved " + tags);

			PhotoRetriever pr = new PhotoRetriever();
						
//			String artistName = event.getArtist().get(0).getName().replaceAll("\\s", "");
			
			List<MediaFeedData> mediaList = new ArrayList<MediaFeedData>();
			
//			for(String tag : tags)
//				mediaList.addAll(pr.getMedia2(tag, 
//						event.getVenue().getLatitude(), event.getVenue().getLongitude(),
//						event.getDatetime().minusHours(6), event.getDatetime().plusHours(18),
//						5000L, 10));
			
			mediaList = pr.getMedia4bis(tags, 
					event.getVenue().getLatitude(), event.getVenue().getLongitude(),
					event.getDatetime().minusHours(1), event.getDatetime().plusHours(11),
					5000L, 28);
			
			Collections.sort(mediaList,new CustomComparator());

			for (MediaFeedData mediaFeedData : mediaList) {
//log.debug((new DateTime(Long.parseLong(mediaFeedData.getCreatedTime())*1000)).toString());
//log.trace(mediaFeedData.getLikes().getCount());
//log.trace(mediaFeedData.getTags());
				String url = mediaFeedData.getImages()
						.getLowResolution().getImageUrl();
				urlList.add(url);
			}          
			request.setAttribute("urlList",urlList);
			return "photoAlbum";  
		}
	
	@RequestMapping("/recent")
	  public String getRecentEvent(@ModelAttribute("daysBefore") int daysBefore, 
			  HttpServletRequest request, HttpServletResponse response) throws Exception {  

		  PhotoService pS = new PhotoService();
		  ArrayList<Event> eventsToPage = new ArrayList<Event>();
		  LocalDate localDate = (new LocalDate()).minusDays(1);
		  
		  ArrayList<Event> events = new ArrayList<Event>();
		  
		  events = eS.getTodaysEvents(localDate.minusDays(daysBefore));
		
		  for(Event e : events)
			  if(pS.existsAlmostsOne(e.getId()))
				  eventsToPage.add(e);
		  
		  if(eventsToPage.size() == 0) {
			  return "empty-events";
		  }

		  request.setAttribute("eventList", eventsToPage);
		  request.setAttribute("daysBefore",daysBefore);
		  
log.trace("total events to page " + eventsToPage.size() + " for "
		+ daysBefore + " days before now");
		  return "recent-events";  
	}
	
	  @RequestMapping("/local")
	  public String getLocal(@ModelAttribute("eventId") String eventId, 
			  ArrayList<String> urlList, HttpServletRequest request, 
			  HttpServletResponse response) throws Exception {

		  PhotoService pS = new PhotoService();
		  
		  int intEventId = Integer.parseInt(eventId);
		  
		  ArrayList<Photo> photos = pS.findAllByEventId(intEventId);
		  
		  if(photos.size() == 0)
			  return "empty-results";
		  
		  for (Photo p  : photos) {
			  String url = p.getUrlLinkLow();
log.trace(p.getUrlLinkLow());
			  urlList.add(url);
		  }          
		  request.setAttribute("urlList",urlList);			
log.trace("photos to page " + photos.size());		  		  
		  return "photoAlbum";
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
