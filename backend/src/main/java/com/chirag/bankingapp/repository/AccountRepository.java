package com.chirag.bankingapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chirag.bankingapp.entity.Account;

//extends to JpaRepository interface JpaRepository<Entity, PK type>
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{

}
