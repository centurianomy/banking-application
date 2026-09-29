package com.chirag.bankingapp.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
	@NotBlank(message="password is required")
	//spring annotation for implementing min pass length constraint
	@Size(min=8, message="password must be atleast 8 characters long")
	private String password;
	
}
