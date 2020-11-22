package com.panthers.main;

import com.panthers.main.jpa.JobEntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@SpringBootApplication
public class PanthersApplication {

	public static void main(String[] args) {
		SpringApplication.run(PanthersApplication.class, args);
	}

}
