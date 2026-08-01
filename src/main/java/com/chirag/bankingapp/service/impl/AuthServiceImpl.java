package com.chirag.bankingapp.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chirag.bankingapp.dto.request.LoginRequest;
import com.chirag.bankingapp.dto.request.RegisterRequest;
import com.chirag.bankingapp.entity.Customer;
import com.chirag.bankingapp.entity.UserCredentials;
import com.chirag.bankingapp.exception.CustomerNotFoundException;
import com.chirag.bankingapp.exception.InvalidCredentialsException;
import com.chirag.bankingapp.repository.CustomerRepository;
import com.chirag.bankingapp.repository.UserCredentialsRepository;
import com.chirag.bankingapp.service.AuthService;
import com.chirag.bankingapp.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //for auto dependency injection
public class AuthServiceImpl implements AuthService {

    private final CustomerRepository customerRepository;
    private final UserCredentialsRepository userCredentialsRepository;
    private final PasswordEncoder passwordEncoder;
    //add JwtUtil as dependency
    private final JwtUtil jwtUtil;

    @Override
    public String register(Long customerId, RegisterRequest request) {

        // 1. fetch the customer, throw if not found
        Customer customer = customerRepository.findById(customerId)
        			//customer exeption write here 
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: " + customerId));

        // 2. hash the password
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        // 3. build UserCredentials — username = customer's email, hashed password, linked customer
        UserCredentials credentials = UserCredentials.builder()
                .userName(customer.getEmail())
                .password(hashedPassword)
                .customer(customer)
                .build();

        // 4. save it
        userCredentialsRepository.save(credentials);

        // 5. return a confirmation message
        return "Registration successful for " + customer.getEmail();
    }
    
    //login- generate and return a real token
	@Override
	public String login(LoginRequest request) {
		UserCredentials credentials = userCredentialsRepository.findByUserName(request.getUserName())
				.orElseThrow(() -> new InvalidCredentialsException("Invalid username or password."));
		boolean passwordMatches = passwordEncoder.matches(request.getPassword(), credentials.getPassword());
		if(!passwordMatches) {
			throw new InvalidCredentialsException("Invalid username or password.");
		}
		return jwtUtil.generateToken(credentials.getUserName());
	} 
}

/*codeline breakdown: String hashedPassword = passwordEncoder.encode(request.getPassword());
	request.getPassword()-> gets the pass from user through postman/UI
	encode(request.getPassword()-> encode() is a method which is responsible for encoding the raw pass into hashed pass
	passwordEncoder.encode(request.getPassword())-> encode() method is called using an passwordEncoder obj.
	String hashedPassword;-> stores the hashed password
*/