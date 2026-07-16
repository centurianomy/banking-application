package com.chirag.bankingapp.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.chirag.bankingapp.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponse {
	private String name; //name
	
	private String fatherName; //father's name
	
	private String address; //address
	
	private String email; //email
	
	private String adharId; //adhar id
	
	private String phoneNo; //phone no
	
	private LocalDate dob; //DOB
	
	private Gender gender; //gender
	
	private Long customerId; //customer id
	
	private LocalDateTime createdAt; //created date & time
}
