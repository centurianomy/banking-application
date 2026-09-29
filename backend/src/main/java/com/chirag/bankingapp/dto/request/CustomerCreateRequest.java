package com.chirag.bankingapp.dto.request;

import java.time.LocalDate;

import com.chirag.bankingapp.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Data contains @Getter/@Setter inside, no need to explicitely write them
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerCreateRequest {
	
	@NotBlank(message="name is required")
	private String name; //name
	
	@NotBlank(message="father name is required")
	private String fatherName; //father's name
	
	@NotBlank(message="address is required")
	private String address; //address
	
	@Email(message="enter valid email")
	@NotBlank(message="email is required")
	private String email; //email
	
	@NotBlank(message="adharId is mandatory")
	@Pattern(regexp = "^\\d{12}$", message = "Aadhar ID must be exactly 12 digits")
	private String adharId; //adhar id
	
	@NotBlank(message="phone number is required")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number")
	private String phoneNo; //phone no
	
	@Past(message="Date value must be strictly before today")
	@NotNull(message="DOB is required")
	private LocalDate dob; //DOB
	
	@NotNull(message="Gender is required")
	private Gender gender; //gender
	
}
