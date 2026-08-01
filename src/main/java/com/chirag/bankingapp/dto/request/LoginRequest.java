package com.chirag.bankingapp.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
	@Email(message="username must be a valid email")
	@NotBlank(message = "username is required")
	private String userName;
	@NotBlank(message="password is required")
	private String password;
}
