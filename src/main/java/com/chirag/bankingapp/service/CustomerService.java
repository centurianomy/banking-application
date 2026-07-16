package com.chirag.bankingapp.service;

import com.chirag.bankingapp.dto.request.CustomerCreateRequest;
import com.chirag.bankingapp.dto.response.CustomerResponse;

public interface CustomerService {
	CustomerResponse createCustomer(CustomerCreateRequest request);
	CustomerResponse getCustomerById(Long customerId);
}
