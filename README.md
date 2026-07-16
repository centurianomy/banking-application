#S1 Statrt by creating required Entities
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

##Classes
-GlobalExceptionHandler.java
-ErrorResponse.java

##Validation test
###Input: POST
```
{
  	"name": "",
  	"email": "not-an-email",
  	"phoneNo": "12345",
  	"adharId": "123"
}
```

###Output: Response
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












