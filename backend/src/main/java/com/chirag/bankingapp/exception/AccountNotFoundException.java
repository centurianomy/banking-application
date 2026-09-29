package com.chirag.bankingapp.exception;

public class AccountNotFoundException extends RuntimeException {
	public AccountNotFoundException(String message){
		super(message);
	}
}
	