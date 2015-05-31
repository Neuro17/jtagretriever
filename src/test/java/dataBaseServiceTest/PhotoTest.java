package dataBaseServiceTest;

import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dataBaseService.Photo;
import dataBaseService.PhotoService;

public class PhotoTest {
	
	private static final Logger log = LogManager.getLogger(PhotoTest.class);
	
	public static void main(String args[]) throws Exception{
		PhotoService pS = new PhotoService();
		
		Photo p = new Photo(8227587, "8", null, null);
//		pS.persist(p);
		
//		pS.delete(8227587,"8");
		
//		ArrayList<Photo> photos = pS.findAllByEventId(7517829);
		
//		ArrayList<Photo> photos = pS.findAll();
//		for(Photo ph : photos)
//			log.trace(ph);
		
//		pS.deleteAll();

	}
}
