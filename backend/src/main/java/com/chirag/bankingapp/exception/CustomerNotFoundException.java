package com.chirag.bankingapp.exception;

public class CustomerNotFoundException extends RuntimeException{
	public CustomerNotFoundException(String message) {
		super(message);
	}
}
