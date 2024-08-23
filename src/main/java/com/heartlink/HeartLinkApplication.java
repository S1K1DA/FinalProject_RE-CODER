package com.heartlink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HeartLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeartLinkApplication.class, args);
    }

}
