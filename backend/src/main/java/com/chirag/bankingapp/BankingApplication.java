package com.chirag.bankingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}
	//paste yout test code here with @Bean  
	
}

// entity → DTO → mapper → service → controller → real HTTP response -> repo -> DB 