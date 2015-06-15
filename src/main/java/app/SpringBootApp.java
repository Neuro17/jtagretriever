package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
		"app.controllers",
		"app.dao",
		"app.repository",
		"app.scheduler",
		"app.twitter",
		"app.tools"})
@EnableAutoConfiguration
@EnableScheduling
public class SpringBootApp {

    public static void main(String[] args) {
    	ApplicationContext ctx = SpringApplication.run(SpringBootApp.class, args);
    }
}
