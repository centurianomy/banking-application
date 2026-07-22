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
public class TransferRequest {
	
	@NotNull(message="receiving account id is required")
	//why toAccountId and not accountId?
	private Long toAccountId;
	@Positive(message="amount should be positive")
	@NotNull(message="amount is required")
	private BigDecimal amount;
}
/*Reason: 
	Think about why this matters: 
	the sending account's ID already comes from the URL path (/accounts/{accountId}/transfer)
	that accountId path variable already exists in your controller method signature. 
	If this DTO field is also named accountId, 
	it's genuinely ambiguous and confusing: 
	does it mean the sender or the receiver? 
	Naming it toAccountId makes the direction explicit and unambiguous.
	"the account money is going to."
*/