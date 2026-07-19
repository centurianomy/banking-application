package com.chirag.bankingapp.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WithdrawRequest {
	@Positive(message="amount should be greater than zero")
	@NotNull(message="amount is required")
	private BigDecimal amount; //amount to withdraw
}

