Money Transfer API
==================

## Overview

The Money Transfer API is a RESTful service built with **Java 17** and **Spring Boot 3.1.4**. It allows you to:

- Schedule new bank transfers
- Update existing transfers (amount and scheduled date)
- Delete transfers
- Retrieve transfer details

Transfer fees are automatically calculated based on the transfer amount and the number of days between **today** and the **scheduled date**, using well-defined business rules encapsulated in the domain.

The project follows **Hexagonal Architecture** principles with clear separation between:

- **adapter/** – web layer (controllers, DTOs, mappers, error handling)
- **application/** – use cases, services, ports
- **domain/** – pure domain models and fee calculation logic

## Technology Stack

- Java 17
- Spring Boot 3.1.4 (Web, Data JPA, Validation)
- PostgreSQL (via Docker)
- Flyway for database migrations
- JUnit 5 and Mockito for tests
- springdoc-openapi for Swagger UI

## Prerequisites

Before running the application, ensure you have:

- JDK 17 installed
- Maven installed (`mvn -v` works)
- Docker installed and running (for PostgreSQL)

## 1. Start the Database

From the project root, start PostgreSQL with Docker Compose:

```bash
docker-compose up -d postgres
```

The default DB configuration (see `src/main/resources/application.yml`):

- URL: `jdbc:postgresql://localhost:5432/money_transfer`
- User: `postgres`
- Password: `postgres`

Flyway will automatically apply the migration script to create the `transfer` table on application startup.

## 2. Run the Application

From the project root, you can run the app in either of two ways.

### Option A – Using Maven

```bash
mvn spring-boot:run
```

### Option B – Using the JAR

```bash
mvn clean package
java -jar target/moneytransfer-0.0.1-SNAPSHOT.jar
```

By default, the API will be available at:

- `http://localhost:8080`

## 3. Explore the API (Swagger UI)

With the application running, open Swagger UI in your browser:

- `http://localhost:8080/swagger-ui/index.html`

There you can:

- Inspect the endpoints
- Send requests for creating, updating, deleting, and retrieving transfers

## 4. Example Requests

### Create a Transfer

POST `http://localhost:8080/api/v1/transfer`

Request body:

```json
{
	"sourceAccount": "ACC123",
	"destinationAccount": "ACC456",
	"amount": 1000,
	"scheduledDate": "2026-02-01"
}
```

Validation is applied at the controller/DTO level using Bean Validation annotations (`@NotBlank`, `@NotNull`, `@Positive`, etc.). Invalid input will return a structured `ApiErrorResponse` with details.

## 5. Run Tests

To execute all unit tests:

```bash
mvn test
```

The test suite currently covers the main application flows, including:

- `FeeCalculatorService` fee rules and error cases
- `TransferService` create, get, update, and delete behaviors
- `TransferController` HTTP endpoints and validation handling

## Notes and Assumptions

- Due to limited business information, **updates** are assumed to change only the **amount** and **scheduledDate** of a transfer. In a real-world scenario, this would be clarified with domain experts.