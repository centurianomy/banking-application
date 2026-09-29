package com.chirag.bankingapp.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.chirag.bankingapp.enums.AccountStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Version;
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
public class Account {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long accountId; //surrogate PK
	
	@Column(unique=true, nullable=false)
	private String accountNo;
	
	@Column(nullable=false)
	private String city;
	
	@Column(nullable=false)
	private String branch;
	@Column(nullable=false)
	private String ifscCode; //same for all the customers
	
	@Column(precision=19, scale=2)
	private BigDecimal balance;
	@Column(precision=19, scale=2)
	private BigDecimal minBalance;
	
	@Column(nullable=false)
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	@Column(nullable=false)
	private LocalDateTime createdAt;
	@PrePersist
	protected void onCreate() {
	    this.createdAt = LocalDateTime.now();
	} 
	
	@ManyToOne
	@JoinColumn(name="customer_id", nullable=false) // creates a FK col "customer_id" in account table
	private Customer customer; 
	
	@Version //the mechanism which detects conflict
	private Long version;

}

/*Note: Mechanism  
 this is the update query thats going on for both A and B Threads

	--UPDATE account
	SET balance = 1200.00, version = 1
	WHERE account_id = 4 AND version = 0--

	both thinks that the version is 0 and the one reaches the DB first wins,
	and sets the [version=1] and when second one arrives it doesnt match with the WHERE clause condition
	as the version is now 1, so automaticlally ignored/rejected 

*/