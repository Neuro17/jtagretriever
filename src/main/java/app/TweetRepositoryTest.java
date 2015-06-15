package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;

import app.models.Tweet;
import app.models.TweetKey;
import app.repository.TweetRepository;

@SpringBootApplication
@ComponentScan(basePackages = "app.repository")
@EntityScan(basePackages = "app.models")
@EnableAutoConfiguration
public class TweetRepositoryTest implements CommandLineRunner {

    @Autowired
    TweetRepository tweet;

    public static void main(String[] args) {
        SpringApplication.run(TweetRepositoryTest.class);
    }

    @Override
    public void run(String... strings) throws Exception {

        for(int i = 0; i < 5; i++){
        	TweetKey pk = new TweetKey();
        	pk.setEventName("The hope");
        	pk.setId((long) i );
        	
        	tweet.save( new Tweet(pk, 40.0 + i, 70.7 + i));
        }
                
        System.out.println("Customers found with findAll():");
        System.out.println("-------------------------------");
        for (Tweet tw : tweet.findAll()) {
            System.out.println(tw);
        }
        System.out.println("custom query");
        
        for (Tweet tw : tweet.findByEventName("The hope")) {
            System.out.println(tw.toString());
        }
        System.out.println();
        
        tweet.toString();
    }

}