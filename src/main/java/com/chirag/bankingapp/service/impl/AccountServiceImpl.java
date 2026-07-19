package com.chirag.bankingapp.service.impl;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.request.DepositRequest;
import com.chirag.bankingapp.dto.request.WithdrawRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;
import com.chirag.bankingapp.entity.Account;
import com.chirag.bankingapp.entity.Customer;
import com.chirag.bankingapp.exception.AccountNotFoundException;
import com.chirag.bankingapp.exception.CustomerNotFoundException;
import com.chirag.bankingapp.exception.InsufficientAccountBalanceException;
import com.chirag.bankingapp.exception.InvalidAccountBalanceException;
import com.chirag.bankingapp.mapper.AccountMapper;
import com.chirag.bankingapp.repository.AccountRepository;
import com.chirag.bankingapp.repository.CustomerRepository;
import com.chirag.bankingapp.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor //for auto dependency injection
public class AccountServiceImpl implements AccountService {
	
	//big decimal decalration
	private static final BigDecimal MINIMUM_OPENING_BALANCE = new BigDecimal("1000.00"); 
	
	//here are two dependecies this AccountService needs both Accnt & Cust Repo
	private final AccountRepository accountRepository;
	private final CustomerRepository customerRepository;
	
	@Override //create account 
	public AccountResponse createAccount(Long customerId, AccountCreateRequest request) {
		//step 1 & 2: fetch the customer, or throw if it doesnt exist
		Customer customer=customerRepository.findById(customerId)
				.orElseThrow(() -> new CustomerNotFoundException("Customer not found with id: "+customerId));
		
		//step 3: enforce the min balance business rule
		if(request.getBalance().compareTo(MINIMUM_OPENING_BALANCE) < 0) {
			throw new InvalidAccountBalanceException("Opening balance must be at least " + MINIMUM_OPENING_BALANCE);
		}
		
		//step 4: built the Account entity, using the fetched Customer
		Account account  = AccountMapper.toEntity(request, customer);
		
		//step 5: save it, then convert back to a respnse DTO
		Account savedAccount = accountRepository.save(account);
		return AccountMapper.toResponse(savedAccount);
	}
	
	@Override //get account by id
	public AccountResponse getAccountById(Long accountId) {
	    Account account = accountRepository.findById(accountId)
	            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
	    return AccountMapper.toResponse(account);
	}
	
	@Override //deposit into account
	public AccountResponse deposit(Long accountId, DepositRequest request) {
	    Account account = accountRepository.findById(accountId)
	            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));

	    //main deposit logic flow: get->add->set
	    account.setBalance(account.getBalance().add(request.getAmount()));
	    //save the deposit amount in repo
	    Account savedAccount = accountRepository.save(account);
	    return AccountMapper.toResponse(savedAccount);
	}
	
	@Override //withdraw from account
	public AccountResponse withdraw(Long accountId, WithdrawRequest request) {
	    Account account = accountRepository.findById(accountId)
	            .orElseThrow(() -> new AccountNotFoundException("Account not found with id: " + accountId));
	    //check for: (balance-withdraw amt) < (minbalance)
	    if (account.getBalance().subtract(request.getAmount()).compareTo(account.getMinBalance()) < 0) {
	        throw new InsufficientAccountBalanceException("Insufficient balance to withdraw " + request.getAmount());
	    }
	    // get->subtract->set
	    account.setBalance(account.getBalance().subtract(request.getAmount()));
	    //save 
	    Account savedAccount = accountRepository.save(account);
	    return AccountMapper.toResponse(savedAccount);
	}
	
}
