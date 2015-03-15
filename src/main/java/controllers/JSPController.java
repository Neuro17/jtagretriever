package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JSPController extends GenericController{

	  @RequestMapping("/jsptest")
	    public String test(ModelAndView modelAndView) {
		  
		  	String test = "test";
		  
//		  	ModelMap model = new ModelMap();
//	        model.addAttribute("test", "test");
//	        return new ModelAndView("user_list", model);
		  	
		  	System.out.println(modelAndView);
	    	
	        return "jsp-spring-boot";
	  }
	
}
