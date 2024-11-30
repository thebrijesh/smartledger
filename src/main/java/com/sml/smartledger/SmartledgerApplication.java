package com.sml.smartledger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SmartledgerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartledgerApplication.class, args);
	}

}
