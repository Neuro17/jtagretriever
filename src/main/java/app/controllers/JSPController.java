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
/*	  
	  @RequestMapping(value = "/photoAlbum", method = RequestMethod.GET)
		public ModelAndView getdata() throws InstagramException {
	 
			PhotoRetriever pr = new PhotoRetriever();
	 
			List<String> tagList = new ArrayList<String>();
			tagList.add("fire");
			tagList.add("entics");
			tagList.add("justintime");
			
			List<MediaFeedData> mfd = pr.getMediaByTagList(tagList);
			
			//return back to photoAlbum.jsp
			ModelAndView model = new ModelAndView("photoAlbum");
			model.addObject("lists", mfd);
	 
			return model;
	 
	  }
*/	  

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

		  return "popular";
	  }
	  
	  @RequestMapping("/search")
	    public String searchPage(@ModelAttribute("tag") String tag, 
	    		ArrayList<String> urlList, HttpServletRequest request) throws InstagramException {
		  
/*	traduce "Pink Floyd" in "PinkFloyd"	*/		  
		  tag = tag.replaceAll("\\s", "");

		  System.out.println("----------------> Requested search for the tag = " + tag);

//TODO effettuare controllo validità tag 
		  
	      /* do some process and send back the data */

		  PhotoRetriever pr = new PhotoRetriever();
        
		  List<MediaFeedData> mediaList = pr.getMediaByTag(tag);
		  
          for (MediaFeedData mediaFeedData : mediaList) {
        	  String url = mediaFeedData.getImages().getLowResolution().getImageUrl();
        	  urlList.add(url);
          }
          
          request.setAttribute("urlList",urlList);
	        
		  return "photoAlbum";
	  }

}
