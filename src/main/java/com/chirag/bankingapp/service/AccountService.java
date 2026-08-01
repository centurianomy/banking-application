package com.chirag.bankingapp.service;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.request.DepositRequest;
import com.chirag.bankingapp.dto.request.TransferRequest;
import com.chirag.bankingapp.dto.request.WithdrawRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;
import com.chirag.bankingapp.dto.response.TransferResponse;

public interface AccountService {
    AccountResponse createAccount(Long customerId, AccountCreateRequest request);
    AccountResponse getAccountById(Long accountId);
    AccountResponse deposit(Long accountId, DepositRequest request);
    AccountResponse withdraw(Long accountId, WithdrawRequest request);
    TransferResponse transfer(Long fromAccountId, TransferRequest request);
    
}