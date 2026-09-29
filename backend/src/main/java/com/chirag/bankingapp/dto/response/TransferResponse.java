package com.chirag.bankingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransferResponse {
	private AccountResponse fromAccount;
	private AccountResponse toAccount;
}
/*	this DTO doesnt hold primitive fields(String, BigDecimal, etc)
	it holds 2 other DTOs as its fields.
Note:   A response obj can be composed of other response objects
 		when the operation genuinely produces two related results!
*/