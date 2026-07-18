package com.chirag.bankingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/customers") /* /customers is a convention, for URL use "plural" forms (although /customer is not wrong) */ 
@RequiredArgsConstructor
public class CustomerController {
	//constructor dependency by lombok
	private final CustomerService customerService;
	
	@PostMapping //create 
	public ResponseEntity<CustomerResponse> createCustomer(@Valid @RequestBody CustomerCreateRequest request){
		CustomerResponse response = customerService.createCustomer(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED); //signal that customer is created 
	}
	
	@GetMapping("/{customerId}")//  @GetMapping
	public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long customerId){
		CustomerResponse response = customerService.getCustomerById(customerId);
		return new ResponseEntity<>(response, HttpStatus.OK); //signal that is is get
	} 
	
	
	//	@PutMapping
	
	
	//	@DeleteMapping
	
}

//@Valid annotation is to validate that all the fields have valid values
//@Valid is the trigger mechanism (goes in the controller, tells Spring "run validation on this DTO")