package com.chirag.bankingapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chirag.bankingapp.entity.Customer;

//extends to JpaRepository interface JpaRepository<Entity, PK type>
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByEmail(String email);
}
