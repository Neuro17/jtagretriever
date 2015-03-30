package app.controllers;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

//@RestController
//@ControllerAdvice
//public class ErrorsHandlerController implements ErrorController{
//
//    private static final String PATH = "/error";
//    
//    public static final String DEFAULT_ERROR_VIEW = "error";
//
////    @RequestMapping(value = PATH)
////    public String error() {
////        return "Error handling";
////    }
//    
//   
//    @RequestMapping(value = PATH)
//    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
//    public String defaultErrorHandler(HttpServletRequest request, Exception e, Map<String, Object> model) {
//    	model.put("datetime", new Date());
//    	model.put("exception", e);
//    	model.put("url", request.getRequestURL());
//        return "error";
//   }
//
//    @Override
//    public String getErrorPath() {
//        return PATH;
//    }
//}