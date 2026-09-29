package com.chirag.bankingapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCredentials {
	@Id //auto generated id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true, nullable=false)
	private String userName;
	@Column(nullable=false)
	private String password;
	
	@OneToOne //one to one relationship with customer
	@JoinColumn(name="customer_id")
	private Customer customer;
	
}
//why no @Data? --> bidirectional relationship risk, unsafe JPA equals/hashCode


