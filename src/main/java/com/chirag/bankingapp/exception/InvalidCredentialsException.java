package com.chirag.bankingapp.exception;
//Custome exception for invalid username or password
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}