package com.ps.kor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class KorApplication {

	/**
	 * Application entry point
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(KorApplication.class, args);
	}
}
