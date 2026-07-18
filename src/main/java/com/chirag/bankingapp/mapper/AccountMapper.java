package com.chirag.bankingapp.mapper;

import java.math.BigDecimal;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;
import com.chirag.bankingapp.entity.Account;
import com.chirag.bankingapp.entity.Customer;
import com.chirag.bankingapp.enums.AccountStatus;
import com.chirag.bankingapp.util.AccountNumberGenerator;


//Account Mapper class
public class AccountMapper {
	public static Account toEntity(AccountCreateRequest request, Customer customer) {
		return Account.builder()
				.city(request.getCity())
				.branch(request.getBranch())
				.balance(request.getBalance())
				.customer(customer)
				
				.accountNo(AccountNumberGenerator.generate())//getting val from AcntNuGenetr method using class name,cause the method is static!
				.accountStatus(AccountStatus.ACTIVE)
				
				//hardcoded val for now, to be automated later
				.ifscCode("SBI0000001")
				.minBalance(new BigDecimal("1000.00")) // matches MINIMUM_OPENING_BALANCE in AccountServiceImpl
				
				.build();
	}
	
	public static AccountResponse toResponse(Account account) {
		return AccountResponse.builder()
				.accountId(account.getAccountId())
				.accountNo(account.getAccountNo())
				.accountStatus(account.getAccountStatus())
				.balance(account.getBalance())
				.minBalance(account.getMinBalance())
				.branch(account.getBranch())
				.city(account.getCity())
				.createdAt(account.getCreatedAt())
				.ifscCode(account.getIfscCode())
				.customerId(account.getCustomer().getCustomerId())
				.build();

	}
		

}
