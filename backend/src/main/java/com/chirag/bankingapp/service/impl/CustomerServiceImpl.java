package com.chirag.bankingapp.service.impl;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chirag.bankingapp.dto.request.CustomerCreateRequest;
import com.chirag.bankingapp.dto.response.CustomerResponse;
import com.chirag.bankingapp.entity.Customer;
import com.chirag.bankingapp.exception.CustomerNotFoundException;
import com.chirag.bankingapp.mapper.CustomerMapper;
import com.chirag.bankingapp.repository.CustomerRepository;
import com.chirag.bankingapp.service.CustomerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //for auto dependency injection
public class CustomerServiceImpl implements CustomerService{
	
	//no need of constr dependncy injection lombok will auto handle
	private final CustomerRepository customerRepository;
	
	//1. override create method
	@Override
	public CustomerResponse createCustomer(CustomerCreateRequest request){
		Customer customer = CustomerMapper.toEntity(request);
		Customer savedCustomer = customerRepository.save(customer);
		return CustomerMapper.toResponse(savedCustomer);
	}
	
	//2. override get by Id
	@Override
	public CustomerResponse getCustomerById(Long customerId) {
		Customer customer = customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: "+customerId));
		//to authorise only owner can access the customer details(SpringSecurity)
		verifyCustomerOwnership(customer);
		
		return CustomerMapper.toResponse(customer);
	}

	//add new
	@Override
public CustomerResponse getCurrentCustomer() {
    String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
    
    Customer customer = customerRepository.findByEmail(currentUsername)
            .orElseThrow(() -> new CustomerNotFoundException("Customer not found for logged-in user"));
    
    return CustomerMapper.toResponse(customer);
}
	
	//helper method to  verify the customer who can access the records
	private void verifyCustomerOwnership(Customer customer) {
	    String currentUsername =
	            SecurityContextHolder.getContext().getAuthentication().getName();

	    if (!customer.getEmail().equals(currentUsername)) {
	        throw new AccessDeniedException(
	                "You do not have permission to access this customer.");
	    }
	}
}


/* @RequiredArgsConstructor is stronger and safer version of dependency injection than a mutable @Autowired field
	Why final matters here?
		a final field must be assigned exactly once, 
		and Java requires it be assigned in every constructor.
	
	By declaring the dependency final,
		you're making it structurally impossible for the object
		to exist in a "half-wired" state
*/

