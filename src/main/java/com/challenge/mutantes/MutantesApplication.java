package com.challenge.mutantes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MutantesApplication {


	public static void main(String[] args) {
		SpringApplication.run(MutantesApplication.class, args);
		System.out.println("Go to http://localhost:8080/swagger-ui.html# to start looking for mutants!");
	}




}
