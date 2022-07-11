package com.example.restservice;

import org.openziti.springboot.ZitiTomcatCustomizer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication (
  scanBasePackageClasses = {ZitiTomcatCustomizer.class,GreetingController.class}
)
public class RestServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestServiceApplication.class, args);
    }

}
