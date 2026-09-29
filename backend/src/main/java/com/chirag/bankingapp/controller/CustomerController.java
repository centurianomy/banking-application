package com.chirag.bankingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chirag.bankingapp.dto.request.CustomerCreateRequest;
import com.chirag.bankingapp.dto.response.CustomerResponse;
import com.chirag.bankingapp.service.CustomerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@CrossOrigin(origins="http://localhost:5173")
@RestController
@RequestMapping("/customers") /* /customers is a convention, for URL use "plural" forms (although /customer is not wrong) */ 
@RequiredArgsConstructor
public class CustomerController {
	//constructor dependency by lombok
	private final CustomerService customerService;
	
	@PostMapping //create customer
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest request){
		CustomerResponse response = customerService.createCustomer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED); //signal that customer is created 
	}
	
	// @GetMapping("/{customerId}")// Get customer_Id
	// public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
	// 	CustomerResponse response = customerService.getCustomerById(customerId);
	// 	return new ResponseEntity<>(response, HttpStatus.OK); //signal that is is get
	// } 
	
	//add new  endpoints
	// Created endpoint 
		@GetMapping("/{customerId:[0-9]+}")
		public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
			CustomerResponse response = customerService.getCustomerById(customerId);
			return new ResponseEntity<>(response, HttpStatus.OK);
		}

		@GetMapping("/me")
		public ResponseEntity<CustomerResponse> getCurrentCustomer() {
			CustomerResponse response = customerService.getCurrentCustomer();
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	
	//	@PutMapping
	
	
	//	@DeleteMapping
	
}

//@Valid annotation is to validate that all the fields have valid values
//@Valid is the trigger mechanism (goes in the controller, tells Spring "run validation on this DTO")
