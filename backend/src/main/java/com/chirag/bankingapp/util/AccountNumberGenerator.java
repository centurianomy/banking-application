package com.chirag.bankingapp.util;

public class AccountNumberGenerator {
	//private constructor manually created!
	private AccountNumberGenerator() {
		// prevents instantiation — this class only exists to hold static methods
	}
	
	//static method- to be called directly with class name, no object
    public static String generate() {
        long timestamp = System.currentTimeMillis(); //inbuilt function for time values in milliseconds
        return "BANK" + timestamp;
    }
}

//System.currentTimeMillis(); 
//inbuilt function for "Current time in milliseconds" from the particular "System"
