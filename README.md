#S1 Start by creating required Entities
-created Customer.java and Account.java classes along with req enums [Gender, AccountStatus]

#S2 Run a small test
-testing by runnning the BankingApplication.java to ensure hibernate creates all the tables as given through @Entity 

#S3 Create Repository interface for both entities
-now create repository for both cust and accnt and extends them to JpaRepository<Entity, PK Type>

-Run a test by adding CommandLineRunner @Bean inside main class.
this is the following code, paste it inside main file:
  
	@Bean
	public CommandLineRunner testRun(CustomerRepository customerRepository, AccountRepository accountRepository){
		return args->{
			//customer builder chain
			Customer customer = Customer.builder()
					.name("Rahul Sharma")
			        .fatherName("Suresh Sharma")
			        .dob(LocalDate.of(1995, 5, 20))
			        .address("123 MG Road, Delhi")
			        .adharId("123456789012")
			        .email("rahul@example.com")
			        .phoneNo("9876543210")
			        .gender(Gender.MALE)
			        .build();			
			//save customer
			Customer savedCustomer = customerRepository.save(customer);
			
			//account builder chain
			Account account = Account.builder()
					.accountNo("abc1239876")
					.city("Noida")
					.branch("SBI")
					.ifscCode("SBI0987654321")
					.balance(new BigDecimal("1000.00")) //while passing val to BigDecimal param always pass a (BigDecimal val)!
					.minBalance(new BigDecimal("500.00"))
					.accountStatus(AccountStatus.ACTIVE)
					.customer(savedCustomer)
					.build();
			//save account
			Account savedAccount = accountRepository.save(account);
			
			//print both
			System.out.println(savedCustomer);
			System.out.println(savedAccount);
		};
	}
	
#S4 Create DTO req and response class for customer
-now create CustomerReq.java and CustomerResponse.java inside DTO pckg and remember response class dont require any validation notation like CustomerCreateReq!

#S4 entity ↔ DTO conversion code 
-now create a separate mapper class inside a mapper pckg

Q) Mapper
A mapper is a very different, specific, well-known concept in layered architecture: it's dedicated entirely to translating between your domain entities and DTOs

#S5 service/CustomerService.java
Q) Why should CustomerService's method signature use DTOs (CustomerCreateRequest → CustomerResponse), never a raw Customer entity?
Ans) coupling and control over what the client can send/receive.
if a controller method accepted a raw Customer in a request, a client could set customerId or other fields they shouldn't control.

#S6 service.impl/CustomerServiceImpl.java
-override the methohds which are declared inside theService interface

#S7 CustomerController
-use lombok for injecting Service dependency auto
Do u ever think which dependency to inject and where?
Sol: what u understand by dependency!
     lets know the Flow 


#UI--> Controller --[needs]--> Service --[needs]--> Repository-->DB      
By this flow we can say that [Controller] needs [Service] as an interface to interact with [Repo]
 similarly [Service] needs [Repo] to interact with DB so here both "Service" and "Repo" are dependencies for "Controller" and "Repository" respectively!
 so inside [Controller] class we need to inject [Service] dependency and inside [ServiceImpl] we need to inject [Repo] dependency... 

#S8 Run a test through Postman for POST

flow: entity → DTO → mapper → service → controller → real HTTP response

#S9 Create Custom Exceptions
for better readability and clear message instead of passing all the info about the pckg and classes

###Classes
-GlobalExceptionHandler.java
-ErrorResponse.java

###Validation test
####Input: POST
```
{
  	"name": "",
  	"email": "not-an-email",
  	"phoneNo": "12345",
  	"adharId": "123"
}
```

####Output: Response
```
{
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed",
    "timestamp": "2026-07-12T20:00:43.4380676",
    "fieldErrors": {
        "fatherName": "father name is required",
        "address": "address is required",
        "gender": "Gender is required",
        "dob": "DOB is required",
        "name": "name is required",
        "adharId": "Aadhar ID must be exactly 12 digits",
        "email": "enter valid email",
        "phoneNo": "Phone number must be a valid 10-digit Indian mobile number"
    		}
} 
```

#S10 now add a customer by Id  

insert CstomerById method inside CustomerService.java interface 
override that method in CustomerServiceimplementation.java
now add a new method inside CustomerController.java @GetMapping("/{customerId}")

test it using valid id and non-existing id and check for the messages returned.

#S11 
Question 3: Should the client be able to send any balance value they want when opening an account, or should there be a rule tying it to minBalance? And — is that the kind of check a Bean Validation annotation can do on its own, or does it need real logic somewhere else?

Answer: no, not with the standard built-in annotations (@Min, @Max, @Positive, etc. only check a field against a fixed number you hardcode into the annotation itself, e.g. @Min(0)). Bean Validation can do cross-field validation, but it requires writing a custom validation annotation — genuinely more advanced than what we've covered so far, and honestly overkill for this specific rule.

The simpler, more appropriate answer for this case: since we're reconsidering whether minBalance should even be client-provided (see above — it might just be a fixed system constant like 1000.00), the actual check becomes: "is the client's opening balance ≥ the system's fixed minimum?" — and that's a business rule comparison, which belongs in the service layer as a plain if statement, not a DTO annotation at all.
 This connects back to something we discussed early on: not everything belongs in Bean Validation — validation annotations check shape/format of input, while business rules that require decision-making or reference to other data belong in the service layer.

Conclusion: The minimum balance check is the second kind — it's a business rule, not a formatting check. thats the reason it will be inside Service class and not DTO!

## create AccountCreateRequest.java
add the field which a custoemr should provide to the bank for this project those are city, branch and balance.

Question: balance — this is a BigDecimal. What annotation ensures it's not null (remember — @NotBlank only works on String/CharSequence, so it can't be used here)? 

AccountMapper class
AccountService interface
AccountServiceImpl class
AccountInvalidBalanceException class
AccountController class
and add another method AccountInvalidBalanceException inside GlobalExceptionHandler class
 for creating a unique account number add a class named AccountNumberGenerator inside utility packg.
 caveat: inside acntgenerator class the generate() method will be static and declare a private construct to prevent obj creation of the class!
 

#S12 build the full "Get Account by ID" feature

AccountNotFoundException.java (new)
New method in AccountService interface: AccountResponse getAccountById(Long accountId);
New method in AccountServiceImpl — fetch via .orElseThrow(...), throw AccountNotFoundException, convert via AccountMapper.toResponse(...)
New method in AccountController — @GetMapping("/{accountId}") (careful with the URL — should this be nested under /customers/{customerId}/accounts/{accountId}, or a flatter /accounts/{accountId}? Think about it — once you have a specific account ID, do you still need the customer ID in the URL at all to uniquely identify it?)
New handler in GlobalExceptionHandler for AccountNotFoundException, 404

#S13 Create deposit and Withdraw feature
DepositeRequest.java class
add deposit method inside AccountService interface
and override it inside AccountServiceImpl class
also update AccountController.java class

Run a test through Postman for:
1. Positive amount
2. Negative amount
3. Missing amount
4. Non-existing account

follow same for Withdraw operation...

#S14 Transaction process [critical phase...]
##### @Transactional is not optional here — without it, Spring/JDBC would just execute each save() independently, with no guarantee that a failure partway through undoes what already happened. 
@Transactional is the mechanism that gives you atomicity in Spring.

@Transactional This is what wraps the entire method in a single database transaction.
if an exception is thrown anywhere in this method (including our own InsufficientAccountBalanceException, or a genuine crash), any database changes already made in this method get rolled back automatically.

Note:
use this import- import org.springframework.transaction.annotation.Transactional;
Avoid this import- import jakarta.transaction.Transactional;

Reason: it's the convention

Why this matters: Spring's own version has more configuration options specific to Spring's transaction management — things like rollbackFor, noRollbackFor, propagation, isolation levels (we might touch these later for the concurrency discussion). The jakarta.transaction version is more generic/portable across different Java EE-style frameworks, but in a Spring Boot project, you'd typically see org.springframework.transaction.annotation.Transactional used almost everywhere. 

create new TransferRequest DTO
update AccountService, AccountServiceImpl, AccountController class.

#S15 add @Version fields and new custom exception 
added custom exception:
ObjectOptimisticLockingFailureException.java

#S16 unit testing for concurrency simulation
write a simple JUnit test code with @Test annotation for confirming the concurrency occurence with two Threads A and B 

#S17 Spring Security [Session/JWT(JSON Web Token)]
###-difference between Session and JWT
 Session - Statefull
 JWT - Stateless 
###Q) Why JWT is prefered?
The key limitation, and why JWT exists for REST APIs specifically:
with sessions, the server must remember every logged-in user, session state lives on the server. 
If you have multiple server instances (common in real, scaled systems load-balanced across several machines), 
every server instance needs access to that same session data, 
which adds real complexity (shared session stores, sticky sessions, etc.).

##-Definition:
JWT is a self-contained, digitally signed token that carries the user's identity information inside itself.
the server doesn't need to "remember" anything about who's logged in.

#S18 Sprinng Security using JWT
UserCredentials.java inside entity pckg for storing the user credentials like id, username, password and customer.

we use hashing for storing the pass of customers.
and and hashed pass can never be reversed!!!

-User during first time registration types the pass and that pass is converted into hashed form and is stored in DB and next time the user enter his password in normal string the server converts that input into the hash form and check it with the password already stored in DB.

the practical tool: Spring Security's "BCryptPasswordEncoder"

BCrypt specifically is a special-purpose password hashing algorithm, and it deliberately adds one extra ingredient: a random "salt."

What "salt" actually means, concretely
Every time you call .encode("hello123"), BCrypt generates a brand new random salt (a random string) and mixes it into the hashing process, then stores that salt as part of the output string itself.

RegisterReques.java - add private password field inside this class with @Size + @NotBlank annotation

### Add the Spring Security dependency
	<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>


### Important heads-up before you add this: 
	the moment you add spring-boot-starter-security and restart your app, Spring Security auto-activates and locks down every single endpoint by default — including all your existing, working endpoints (/customers, /accounts, deposit, withdraw, transfer).
	
#S19 Create separate classes and interfaces for user Registration & Authentication
UserCredentialsRepository
SecurityBeansConfig --> (new)
AuthService
AuthServiceImpl
AuthController

right now, /customers/{customerId}/register will also be blocked by Spring Security's default lockdown.
Note: (everything is closed by default right now) so testing this specific endpoint won't work yet until we write the security configuration that explicitly allows it.

#S20 Add JWT dependency in pom.xml
### JWT dependency
	```<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.6</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.6</version>
    <scope>runtime</scope>
</dependency>

### JJWT (Java JSON Web Token)
jjwt ("Java JWT") is just a specific Java library that implements that standard for you — it provides ready-made methods to build a JWT (embed data, sign it, produce the final token string) and to parse/verify one (check the signature, extract the data back out) — so you don't have to hand-write the cryptographic signing logic yourself.


#S21 fix the applicqtion.properties file 

### it should look like this :avoid using real passwords for both DB and JWT secret keys!!!
spring.application.name=bankingapp
spring.datasource.url=jdbc:mysql://localhost:3306/banking_db
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

jwt.secret=${JWT_SECRET}
jwt.expiration=3600000


###set the sercet keys eniv on your own machine using Window Powershell

add  JwtUtil class inside util packg
now add the JwtUtil as a dependency

### additional files
A custom JWT filter (JwtAuthenticationFilter.java)
A security Configuration class 

##test with the endpoints
Test 1:
POST http://localhost:8080/customers/customer_id/register  
{ "password":"mypassword123" }

Test 2:
POST
http://loocalhost:8080/auth/login
{ 
	"username":"customer_email"
	"password":"customer_password"	
}
 
Output Response:
a very long real JWT token
something that looks like three dot-separated chunks of random-looking characters
token format: header.payload.signature
Note: never share your sensitive info into payload as it can be easily reversed,
Signature is actually responsibole for security!
 
Test 3:
GET
http://loocalhost:8080/accounts/account_id
expected 401

Test 4:
GET
http://loocalhost:8080/accounts/account_id
enter JWT token 
output response: customer account details

#Authorization 
Customer A cannot access Customer B details!
need to check whether the mentioned account belongs to the customer or not.
add Helper method inside AccountServiceImpl:
this gets the username/email of the currently logged-in user then compares the logged-in users email with Account owner email if they odnt match the request is denied.
Add A custom AccessDeniedException.java inside GlobalExceptionHandler.java
