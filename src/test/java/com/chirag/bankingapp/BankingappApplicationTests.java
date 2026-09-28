package com.chirag.bankingapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.chirag.bankingapp.dto.request.AccountCreateRequest;
import com.chirag.bankingapp.dto.request.CustomerCreateRequest;
import com.chirag.bankingapp.dto.request.WithdrawRequest;
import com.chirag.bankingapp.dto.response.AccountResponse;
import com.chirag.bankingapp.dto.response.CustomerResponse;
import com.chirag.bankingapp.entity.Account;
import com.chirag.bankingapp.enums.Gender;
import com.chirag.bankingapp.repository.AccountRepository;
import com.chirag.bankingapp.repository.CustomerRepository;
import com.chirag.bankingapp.service.AccountService;
import com.chirag.bankingapp.service.CustomerService;

@SpringBootTest
class BankingappApplicationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void concurrentWithdrawals_shouldNotCorruptBalance() throws InterruptedException {

        CustomerCreateRequest customerRequest = CustomerCreateRequest.builder()
                .name("Test User")
                .fatherName("Test Father")
                .address("Test Address")
                .email("testuser@example.com")
                .adharId("111122223333")
                .phoneNo("9999999999")
                .dob(LocalDate.of(1995, 1, 1))
                .gender(Gender.MALE)
                .build();

        // fake login in the MAIN thread, needed before createAccount's ownership check
        UsernamePasswordAuthenticationToken mainThreadAuth = new UsernamePasswordAuthenticationToken(
                customerRequest.getEmail(), null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(mainThreadAuth);

        CustomerResponse customerResponse = customerService.createCustomer(customerRequest);

        AccountCreateRequest accountRequest = AccountCreateRequest.builder()
                .city("Test City")
                .branch("Test Branch")
                .balance(new BigDecimal("2000.00"))
                .build();

        AccountResponse accountResponse = accountService.createAccount(customerResponse.getCustomerId(), accountRequest);

        Long accountId = accountResponse.getAccountId();

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch readyLatch = new CountDownLatch(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch doneLatch = new CountDownLatch(threadCount);

        WithdrawRequest withdrawRequest = WithdrawRequest.builder()
                .amount(new BigDecimal("800.00"))
                .build();

        Runnable withdrawTask = () -> {
            // fake login again, per worker thread — SecurityContext is thread-local
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    customerRequest.getEmail(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(auth);

            readyLatch.countDown();
            try {
                startLatch.await();
                accountService.withdraw(accountId, withdrawRequest);
            } catch (Exception e) {
                System.out.println("Withdrawal failed: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            } finally {
                doneLatch.countDown();
            }
        };

        executor.submit(withdrawTask);
        executor.submit(withdrawTask);

        readyLatch.await();
        startLatch.countDown();
        doneLatch.await();
        executor.shutdown();

        Account finalAccount = accountRepository.findById(accountId).orElseThrow();

        assertEquals(new BigDecimal("1200.00"), finalAccount.getBalance());
    }
}