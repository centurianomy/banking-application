package com.chirag.bankingapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chirag.bankingapp.dto.request.LoginRequest;
import com.chirag.bankingapp.dto.request.RegisterRequest;
import com.chirag.bankingapp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
//Authorisation controller
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    //for register
    @PostMapping("/customers/{customerId}/register")
    public ResponseEntity<String> register(@PathVariable Long customerId, @Valid @RequestBody RegisterRequest request) {
        String message = authService.register(customerId, request);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    
    //for login
    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        String message = authService.login(request);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
