package app.scheduler;

import java.util.ArrayList;

import javabandsintown.entity.Event;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dataBaseService.EventService;
import app.tools.Task;
import app.twitter.TwitterConnector;

@Component
@EnableAsync
public class ScheduledTasks {
    
    private static final Logger log = LogManager.getLogger(ScheduledTasks.class);
    
    @Autowired 
    TwitterConnector twitter;
        
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
    
    @Scheduled(cron = "0 0 0 1 * *")
    public void artistsUpdate() throws Exception{
    	log.trace("Updating Artists Data");
    	
    	Task.updateDBAritsts();
    }
    
    @Scheduled(cron = "0 0 0 * * 1")
    public void collectFutureArtistsEvent() throws Exception{
    	log.trace("Collecting future artists events data");
    	
    	DateTime start = new DateTime();
    	DateTime end = start.plusDays(7);
    	
		Task.updateArtistsEvents(start, end);
    }
    
    @Scheduled(cron = "0 00 12 * * *")
    public void collectYesterdayPhotos() throws Exception{
    	log.trace("Collecting yesterday concerts images");
    	    	
    	Task.collectDaysAgoDBEventsPhotos(1);
    }
}