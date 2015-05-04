package app.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.twitter.TwitterConnector;
import app.twitter.impl.TwitterConnectorImpl;
import dataBaseService.EventService;
import entity.Event;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    @Autowired 
    TwitterConnector twitter;

//    @Scheduled(fixedRate = 5000)
//    public void reportCurrentTime() {	
//        System.out.println("The time is now " + dateFormat.format(new Date()));
//    }
    
    
    
    /**
     * This task start at midnight of every day.
     * The pattern is a list of six single space-separated fields: representing
     * (second, minute, hour, day, month, weekday).
     * Month and weekday names can be given as the first three letters of the 
     * English names.
     * @throws Exception 
     */
    @Scheduled(cron = "0 46 12 * * *")
    public static void cronExample() throws Exception{
    	System.out.println("this is a cron scheduled task");
    	EventService eventServ = new EventService();
    	LocalDate today = LocalDate.now();
    	ArrayList<Event> events = eventServ.getTodaysEvents(today);
    	System.out.println(events.get(0).toString());
    	TwitterConnectorImpl twc = new TwitterConnectorImpl();
    	twc.StreamConcert(events.get(0), 1);
//    	for(Event e : events){
////    		System.out.println("starting thread");
//    		TwitterConnectorImpl twc = new TwitterConnectorImpl(e, 1);
////    		Thread concert = new Thread(twc, "test");
////    		concert.start();
//    		
//    		twc.StreamConcert(e, 1);
//    		
//    	}
    	
    }
    
    public static void main(String [] args) throws Exception{
    	ScheduledTasks.cronExample();
    }
}