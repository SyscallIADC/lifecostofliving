package scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import database.DatabaseHelper;

@SpringBootApplication
@EnableScheduling
public class Scheduler {
    public static void main(String[] args) {
        DatabaseHelper.getInstance();

        SpringApplication.run(Scheduler.class, args);
    }
}