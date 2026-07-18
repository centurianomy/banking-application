package com.chirag.bankingapp.exception;

public class InvalidAccountBalanceException extends RuntimeException {
	//constructor
	public InvalidAccountBalanceException(String message) {
		super(message); //calls RuntimeException class Constructor and fetch the custom message
	}
}
