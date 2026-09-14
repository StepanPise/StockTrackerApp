package com.stocktrack.stocktrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StocktrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocktrackApplication.class, args);
    }

}
