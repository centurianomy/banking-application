package com.chirag.bankingapp.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountCreateRequest {

	@NotBlank(message="City name is required")
	private String city;
	@NotBlank(message="Branch name is required")
	private String branch;
	@NotNull(message="balance is required")
	@Positive(message="opening balance must be greater than zero")
	private BigDecimal balance;
}


/*
  @NotBlank: only for string and charsequence
  @NotNull: 
 
 
*/