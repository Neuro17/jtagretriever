package app.scheduler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.twitter.TwitterConnector;
import dataBaseService.EventService;
import javabandsintown.entity.Event;

@Component
@EnableAsync
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
    private static final Logger log = LogManager.getLogger(ScheduledTasks.class);
    
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
    @Scheduled(cron = "0 42 19 * * *")
    public void cronExample() throws Exception{
    	log.trace("this is a cron scheduled task");
    	
    	EventService eventServ = new EventService();
    	LocalDate today = LocalDate.now();
    	ArrayList<Event> events = new ArrayList<Event>();
    	
    	events = eventServ.getTodaysEvents(today);
    	
    	for(Event event : events){
    		twitter.StreamConcert(event, 1);
    	}
    
    }
    
    public static void main(String [] args) throws Exception{
//    	ScheduledTasks.cronExample();
    }
}