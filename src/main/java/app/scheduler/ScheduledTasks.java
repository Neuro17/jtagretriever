package app.scheduler;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import dataBaseService.EventService;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
        System.out.println("The time is now " + dateFormat.format(new Date()));
    }
    
    
    
    /**
     * This task start at midnight of every day.
     * The pattern is a list of six single space-separated fields: representing
     * (second, minute, hour, day, month, weekday).
     * Month and weekday names can be given as the first three letters of the 
     * English names.
     */
    @Scheduled(cron = "0 16 0 * * *")
    public void cronExample(){
    	System.out.println("this is a cron scheduled task");
    	EventService eventServ = new EventService();
    	LocalDate today = LocalDate.now();
//    	eventServ.getTodaysEvents(today);
    }
}