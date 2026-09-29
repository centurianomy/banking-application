package com.chirag.bankingapp.mapper;

import com.chirag.bankingapp.dto.request.CustomerCreateRequest;
import com.chirag.bankingapp.dto.response.CustomerResponse;
import com.chirag.bankingapp.entity.Customer;

//Customer Mapper class
public class CustomerMapper {

	public static Customer toEntity(CustomerCreateRequest request) {
		return Customer.builder()
	            .name(request.getName())
	            .fatherName(request.getFatherName())
	            .address(request.getAddress())
	            .email(request.getEmail())
	            .adharId(request.getAdharId())
	            .phoneNo(request.getPhoneNo())
	            .dob(request.getDob())
	            .gender(request.getGender())
	            .build();
    }

    public static CustomerResponse toResponse(Customer customer) {
        	return CustomerResponse.builder()
        			.customerId(customer.getCustomerId())
        			.name(customer.getName())
        			.fatherName(customer.getFatherName())
        			.address(customer.getAddress())
        			.email(customer.getEmail())
        			.phoneNo(customer.getPhoneNo())
        			.adharId(maskAadhar(customer.getAdharId())) //mask adhaar
        			.dob(customer.getDob())
        			.gender(customer.getGender())
        			.createdAt(customer.getCreatedAt())
        			.build();
    }
    
    //custom maskAdhaar method for masking adhaarId and showing only the last 4 digits
    private static String maskAadhar(String adharId) {
        if (adharId == null || adharId.length() != 12) {
            return adharId;
        }
        return "XXXX-XXXX-" + adharId.substring(8);
    }

}
