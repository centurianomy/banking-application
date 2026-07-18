package com.chirag.bankingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;
import com.chirag.bankingapp.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
/* why we dont us @RequestMapping(/accounts) here ???
	No class level mapping needed,
	cause correct path is already fully specified on the method itself. 
*/
@RequiredArgsConstructor
public class AccountController {
	private final AccountService accountService; 
	
	//POST
	@PostMapping("/customers/{customerId}/accounts") 
	public ResponseEntity<AccountResponse> createAccount(@PathVariable Long customerId, @Valid @RequestBody AccountCreateRequest request) {
	    AccountResponse response = accountService.createAccount(customerId, request);
	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
	
	//GET
	@GetMapping("/accounts/{accountId}")
	public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long accountId){
		AccountResponse response = accountService.getAccountById(accountId);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
}


/* @PostMapping("/customers/{customerId}/accounts")
	it shows relation that this particular account belongs to the customer with this custId 
 */