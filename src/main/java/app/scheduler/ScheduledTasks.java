package app.scheduler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.joda.time.DateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import app.tools.Task;

import app.twitter.TwitterConnector;

@Component
@EnableAsync
public class ScheduledTasks {

//    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    
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
    
    public static void main(String [] args) throws Exception{
    	
    }
}