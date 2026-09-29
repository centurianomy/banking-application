package com.chirag.bankingapp.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.chirag.bankingapp.enums.AccountStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
	//field from account obj 
	private Long accountId;
	private String accountNo;
	private String city;
	private String branch;
	private String ifscCode;
	private BigDecimal balance;
	private BigDecimal minBalance;
	private AccountStatus accountStatus;
	private LocalDateTime createdAt;
	//field from customer obj 
	private Long customerId;
}

//here instead of returning a raw customer obj we return customerId as it alone helps in knowing this account belongs to which customer.