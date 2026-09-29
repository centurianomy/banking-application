package com.chirag.bankingapp.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.chirag.bankingapp.enums.Gender;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long customerId; //PK, surrogate, auto-generated
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String fatherName;
	@Column(nullable=false)
	private LocalDate dob;
	@Column(nullable=false)
	private String address;

	@Column(unique=true, nullable=false)
	private String adharId; //unique
	
	@Column(unique=true, nullable=false)
	private String email; //unique
	
	@Column(unique=true, nullable=false)
	private String phoneNo; //unique
	
	@Enumerated(EnumType.STRING)
	@Column(nullable=false)
	private Gender gender;
	
	@Column(nullable=false)
	private LocalDateTime createdAt;
	//method to auto-set the Date and Time for data integrity and consistency we cannot allow customer to edit DateTime by themself! 
	@PrePersist
	protected void onCreate() {
	    this.createdAt = LocalDateTime.now();
	}
	
	//Relation type with Account
	@OneToMany(mappedBy="customer")
	private List<Account> accounts;

}

/*Note: from lombok!
  	avoid using @Data on entities specifically 
	because of bidirectional relationship loops and JPA identity issues!
*/














