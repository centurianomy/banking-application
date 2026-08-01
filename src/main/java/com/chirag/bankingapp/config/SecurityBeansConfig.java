package com.chirag.bankingapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
//this class is to make BCryptPasswordEncoder available for injection wherever needed
@Configuration
public class SecurityBeansConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
//return type- PasswordEncoder (an interface) not BCryptPasswordEncoder (concrete class)
/* Note:
	we declare the return type as PasswordEncoder (an interface), not BCryptPasswordEncoder (the concrete class),
	this is the exact same "program to an interface, not an implementation" principle
	we discussed for Map/HashMap earlier. 
	BCryptPasswordEncoder is just one implementation of the PasswordEncoder contract;
	coding against the interface means you could swap the actual hashing algorithm later
	without changing anything that depends on it. 
 */
 