package controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;



public class GenericController {

	Gson gson = new Gson();
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	protected ResponseEntity<String> createJsonResponse( Object o )
    {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String json = gson.toJson( o );
        return new ResponseEntity<String>( json, headers, HttpStatus.CREATED );
        
    }
    
//    protected User getUsers(){
//		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof String){
//			return null;
//		}else{
//			return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		}
//    }
    
    /**
     * @return true if the user has one of the specified roles.
     */
//    protected boolean hasRoles(String[] roles) {
//        boolean result = false;
//        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
//            String userRole = authority.getAuthority();
//            for (String role : roles) {
//                if (role.equals(userRole)) {
//                    result = true;
//                    break;
//                }
//            }
//
//            if (result) {
//                break;
//            }
//        }
//
//        return result;
//    }
    
//    protected boolean hasRole(String role) {
//        boolean result = false;
//        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
//            String userRole = authority.getAuthority();
//            if (role.equals(userRole)) {
//                    result = true;
//                }
//            if (result) {
//                break;
//            }
//        }
//
//        return result;
//    }
    
    /*
	 * Modificha l'header in base al login.
	 * L'autenticazione fa ricaricare automaticamente la pagina
	 * corrente
	 */
	
	//BIAGIO qua setto le variabili in base alle quali si va a definire l'header
    //per esempio alla variabile authenticated assegno il valore True
    // a username assegno il valore dello username
    // per vedere come sono usate nella jsp guarda in header.jsp
	protected ModelAndView setHeader(ModelAndView model){
//		if(getUsers() == null)model.addObject("authenticated",Boolean.FALSE);
//		else{
//			//
//			model.addObject("authenticated",Boolean.TRUE);
//			model.addObject("username",getUsers().getUsername());
//			
//			//
//		}
//		if(hasRole("ROLE_ADMIN")){
//			model.addObject("admin",Boolean.TRUE);
//		}
		
		return model;
		
	}
	
}
