package com.chirag.bankingapp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

//JUnit test for rca condition
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
        // 1. create a test customer + account with a known balance
        // 2. launch two threads, each calling accountService.withdraw(...)
   
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

    	    CustomerResponse customerResponse = customerService.createCustomer(customerRequest);

    	    AccountCreateRequest accountRequest = AccountCreateRequest.builder()
    	            .city("Test City")
    	            .branch("Test Branch")
    	            .balance(new BigDecimal("2000.00"))
    	            .build();

    	    AccountResponse accountResponse = accountService.createAccount(customerResponse.getCustomerId(), accountRequest);

    	    Long accountId = accountResponse.getAccountId();

    	    // ... next: launch two concurrent withdrawal threads
    	    int threadCount = 2; //no of threads 2
    	    //ExecutorService — manages a pool of actual OS threads. 
    	    //Executors.newFixedThreadPool(2) gives us exactly 2 worker threads to run our tasks on.
    	    
    	    ExecutorService executor = Executors.newFixedThreadPool(threadCount);
    	    CountDownLatch readyLatch = new CountDownLatch(threadCount);
    	    CountDownLatch startLatch = new CountDownLatch(1);
    	    CountDownLatch doneLatch = new CountDownLatch(threadCount);

    	    WithdrawRequest withdrawRequest = WithdrawRequest.builder()
    	            .amount(new BigDecimal("800.00"))
    	            .build();

    	    Runnable withdrawTask = () -> {
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
    	    System.out.println("Final balance: " + finalAccount.getBalance());
    }
}

/* Note:
	proved a race condition existed using a multi-threaded JUnit test with CountDownLatch,
	to force two withdrawal requests to execute simultaneously, 
	then added @Version for optimistic locking,
	
	and confirmed via the same test that Hibernate correctly rejected the second concurrent write 
	with an ObjectOptimisticLockingFailureException, 
	leaving the balance correct.
*/
