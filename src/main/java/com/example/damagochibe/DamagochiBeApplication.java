package com.example.damagochibe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DamagochiBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DamagochiBeApplication.class, args);
    }

}
