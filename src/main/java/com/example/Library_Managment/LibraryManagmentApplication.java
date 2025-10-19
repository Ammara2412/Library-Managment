package com.example.Library_Managment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LibraryManagmentApplication {

	public static void main(String[] args) {

		SpringApplication.run(LibraryManagmentApplication.class, args);
	}

}
//(exclude= {UserDetailsServiceAutoConfiguration.class})