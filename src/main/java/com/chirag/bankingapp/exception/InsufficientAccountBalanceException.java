package com.chirag.bankingapp.exception;

public class InsufficientAccountBalanceException extends RuntimeException{
	public InsufficientAccountBalanceException(String message) {
		super(message);
	}
}
