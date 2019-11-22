package com.nbu.CSCB509;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class Cscb509Application {

	public static void main(String[] args) {
		SpringApplication.run(Cscb509Application.class, args);
	}

}
