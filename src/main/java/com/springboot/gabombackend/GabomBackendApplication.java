package com.springboot.gabombackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class GabomBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GabomBackendApplication.class, args);
	}

}
