# Banking Application

A production-style banking backend built with Spring Boot, modeling core banking
operations — customer onboarding, account management, deposits, withdrawals, and
inter-account transfers — with a focus on data integrity, concurrency safety, and
security, rather than just CRUD functionality.

This project was built incrementally, feature by feature, with an emphasis on
defensible design decisions (why a type was chosen, why a layer exists, why a
particular locking strategy was used) over simply making things "work."

## Features

- **Customer management** — onboarding with KYC-style validation (Aadhar, DOB, contact info)
- **Account management** — account opening with minimum balance enforcement
- **Deposit / Withdraw** — with business-rule validation (minimum balance protection)
- **Fund transfer** — atomic, all-or-nothing transfers between two accounts
- **Concurrency safety** — optimistic locking to prevent race conditions on simultaneous
  balance updates, verified with a multi-threaded JUnit test
- **JWT-based authentication** — stateless login using hashed credentials
- **Ownership-based authorization** — a customer can only access and operate on their
  own accounts, even when authenticated
- **Structured error handling** — every failure mode returns a clean, consistent JSON
  error shape instead of leaking internal stack traces

## Tech Stack

| Category | Technology |
|---|---|
| Language / Framework | Java, Spring Boot |
| Data Access | Spring Data JPA, Hibernate |
| Database | MySQL |
| Security | Spring Security, JWT (jjwt), BCrypt |
| Build Tool | Maven |
| Utilities | Lombok |
| Testing | JUnit 5 |

## Architecture

The project follows a strict layered architecture:

```
Controller → Service (interface + impl) → Repository → Database
                 ↑
              Mapper (Entity ↔ DTO conversion)
```

- **Controller** — handles HTTP concerns only (routing, status codes); contains no
  business logic
- **Service** — orchestrates business rules, validation, and persistence calls
- **Repository** — Spring Data JPA interfaces, no manual SQL required for standard operations
- **Mapper** — manual conversion between entities and DTOs, kept as a separate layer
  so entities are never exposed directly through the API
- **DTOs** — split into `request` and `response` packages; request DTOs carry validation,
  response DTOs never do
- **GlobalExceptionHandler** — a single `@RestControllerAdvice` mapping every domain
  exception to the correct HTTP status and a consistent error response shape

### Entity relationships

- `Customer` 1 — N `Account` (a customer can hold multiple accounts)
- `Customer` 1 — 1 `UserCredentials` (login credentials kept separate from personal
  data, since they represent a different responsibility)

## Key Design Decisions

These are the decisions worth asking about in an interview — each one was made
deliberately, not by default.

- **`BigDecimal`, never `double`, for money.** Floating-point types introduce binary
  rounding errors (`0.1 + 0.2 != 0.3`) that are unacceptable for currency. All monetary
  fields use `BigDecimal` in Java and `DECIMAL(19,2)` in MySQL.

- **Surrogate primary keys, not natural identifiers.** Customer IDs and account IDs
  are auto-generated, meaningless integers — never Aadhar numbers or account numbers.
  This keeps the database's internal structure insulated from real-world data that can
  change or need reformatting.

- **DTOs are a hard boundary.** Entities are never returned from or accepted by a
  controller. This prevents mass-assignment vulnerabilities (a client setting fields
  like `accountId` or `createdAt` directly) and decouples the API contract from the
  database schema.

- **`@Transactional` for atomicity.** Fund transfers touch two account rows in one
  operation. Without a transaction boundary, a failure between the two writes could
  leave money debited from one account and never credited to the other. `@Transactional`
  guarantees both writes succeed or neither does.
  it performs Rollback in case of any abnormal operation.

- **Optimistic locking (`@Version`) for concurrency safety.** Two simultaneous
  withdrawals on the same account could both read the same balance before either
  writes, resulting in an incorrect final balance. A `@Version` column on `Account`
  makes Hibernate detect this conflict at save time and reject the losing write with
  `ObjectOptimisticLockingFailureException`, mapped to `409 Conflict`. This was not
  just implemented but **proven**: a JUnit test using `ExecutorService` and
  `CountDownLatch` fires two genuinely concurrent withdrawal threads at the same
  account and asserts the final balance reflects exactly one successful withdrawal.

- **Passwords are hashed with BCrypt, never stored in plaintext.** BCrypt is a
  one-way, salted hash — the same password produces a different stored hash each
  time, and verification is done by re-hashing a login attempt and comparing outputs,
  never by reversing a hash.

- **JWT for stateless authentication.** No server-side session state is kept; each
  request carries a signed token containing the user's identity, verified on every
  request by a custom `OncePerRequestFilter`.

- **Authentication is not authorization.** A valid JWT proves *who* a user is, but
  every account-touching service method additionally verifies that the authenticated
  user actually **owns** the account being accessed, before any read or mutation
  happens. Cross-customer access attempts return `403 Forbidden`.

- **Structured, safe error responses.** Every exception handler returns a consistent
  `{status, error, message, timestamp}` shape. Messages returned to the client are
  either developer-authored (safe) or deliberately generic (for unexpected/internal
  failures) — raw exception messages from Hibernate or the JVM are never passed
  through to the client.

## API Endpoints

| Method | Endpoint                        | Auth required        | Description |
|--------|---------------------------------|----------------------|-------------|
| POST   | `/customers`                      | No                   | Create a customer |
| GET    | `/customers/{customerId}`           | No                   | Get a customer by ID |
| POST   | `/customers/{customerId}/accounts`   | No                  | Open an account for a customer |
| GET    | `/accounts/{accountId}`            | Yes 				| Get an account by ID (owner only) |
| POST   | `/accounts/{accountId}/deposit`      | Yes 				| Deposit into an account (owner only) |
| POST   | `/accounts/{accountId}/withdraw`     | Yes 				| Withdraw from an account (owner only) |
| POST   | `/accounts/{accountId}/transfer`     | Yes 				| Transfer to another account (sender must be owner) |
| POST   | `/customers/{customerId}/register`   | No  					| Register login credentials for a customer |
| POST   | `/auth/login` 						| No 				| Log in, receive a JWT |

Authenticated requests require an `Authorization: Bearer <token>` header, obtained
from `/auth/login`.

## Setup / How to Run

### Prerequisites
- Java 21
- MySQL running locally
- Maven

### Environment Variables

The application reads sensitive configuration from environment variables — nothing
sensitive is hardcoded in `application.properties`:

```
DB_USERNAME=<your MySQL username>
DB_PASSWORD=<your MySQL password>
JWT_SECRET=<a long, random string used to sign JWTs>
```

### Database

```sql
CREATE DATABASE banking_db;
```

Schema is auto-managed via `spring.jpa.hibernate.ddl-auto=update` for local
development.

### Running the application

```bash
mvn spring-boot:run
```

The application starts on `http://localhost:8080`.

## Testing

- `BankingappApplicationTests` includes a concurrency test that launches two threads
  against the same account simultaneously using `ExecutorService` and
  `CountDownLatch`, proving that optimistic locking correctly rejects the losing
  concurrent write and that the account's final balance is never corrupted.
- All endpoints were additionally verified manually via Postman across success and
  failure paths (validation errors, not-found cases, business rule violations,
  authentication failures, and authorization failures).

## Future Improvements

Documented honestly, as a roadmap rather than a gap list:

- Expand automated test coverage to the service layer (currently one concurrency-focused test exists)
- Pagination on list-style endpoints
- A custom `AuthenticationEntryPoint` to return a consistent `401` (rather than
  Spring Security's default `403`) for missing/invalid tokens
- Dockerize the application for easier setup and deployment
- Move from a hardcoded IFSC code to a proper `Branch` entity with real branch lookup
- Refresh tokens, to avoid requiring re-login on JWT expiry
