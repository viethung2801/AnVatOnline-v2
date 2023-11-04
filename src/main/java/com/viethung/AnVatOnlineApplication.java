package com.viethung;

import com.viethung.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AnVatOnlineApplication {

	public static void main(String[] args) {

		SpringApplication.run(AnVatOnlineApplication.class, args);
	}

}
