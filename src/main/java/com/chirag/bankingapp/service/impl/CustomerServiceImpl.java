package com.chirag.bankingapp.service.impl;

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
		return CustomerMapper.toResponse(customer);
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

