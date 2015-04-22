package app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;
import org.springframework.stereotype.Controller;
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
	    public String searchPage(ModelAndView modelAndView) {

		  return "search";
	  }

}
