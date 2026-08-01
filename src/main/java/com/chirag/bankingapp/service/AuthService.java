package com.chirag.bankingapp.service;

import com.chirag.bankingapp.dto.request.LoginRequest;
import com.chirag.bankingapp.dto.request.RegisterRequest;
//AuthService Interface
public interface AuthService {
	String register(Long customerId, RegisterRequest request);
	String login(LoginRequest request);
}
