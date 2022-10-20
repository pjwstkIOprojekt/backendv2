package com.gary.backendv2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Backendv2Application {

    public static void main(String[] args) {
        SpringApplication.run(Backendv2Application.class, args);
    }

}
