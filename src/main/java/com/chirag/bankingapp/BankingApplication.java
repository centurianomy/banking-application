package com.chirag.bankingapp;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.chirag.bankingapp.entity.Account;
import com.chirag.bankingapp.entity.Customer;
import com.chirag.bankingapp.enums.AccountStatus;
import com.chirag.bankingapp.enums.Gender;
import com.chirag.bankingapp.repository.AccountRepository;
import com.chirag.bankingapp.repository.CustomerRepository;

@SpringBootApplication
public class BankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}
	//paste yout test code here with @Bean  
	
}

// entity → DTO → mapper → service → controller → real HTTP response