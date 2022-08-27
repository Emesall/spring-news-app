package com.emesall.news;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringNewsAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNewsAppApplication.class, args);
	}

}
