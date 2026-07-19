package com.chirag.bankingapp.service;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.request.DepositRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;

public interface AccountService {
    AccountResponse createAccount(Long customerId, AccountCreateRequest request);
    AccountResponse getAccountById(Long accountId);
    AccountResponse deposit(Long accountId, DepositRequest request);
    
}