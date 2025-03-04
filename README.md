
# Customer API

A RESTful API for managing customer data with JWT authentication.

## Overview

Customer API is a Spring Boot application that provides a secure way to manage customer information. It includes features for user authentication, customer CRUD operations, and age-based analytics.

## Features

-   **User Authentication**: Secure JWT-based authentication

-   **Customer Management**: Create, read, update, and delete customer records

-   **Data Validation**: Comprehensive input validation for customer data

-   **Age Analytics**: Calculate average customer age and filter customers by age range

-   **API Documentation**: Swagger UI for easy API exploration

-   **Security**: Role-based access control and password encryption


## Technologies

-   **Spring Boot**: Framework for building production-ready applications

-   **Spring Security**: Authentication and authorization

-   **Spring Data JPA**: Data access and persistence

-   **PostgreSQL**: Relational database for storing customer and user data

-   **JWT**: JSON Web Tokens for stateless authentication

-   **Swagger/OpenAPI**: API documentation

-   **JUnit 5**: Testing framework

-   **TestContainers**: Integration testing with real database instances


## Project Structure

com.example.customerapi/
├── config/                 # Configuration classes
├── controllers/            # REST API endpoints
├── dataTransferObjects/    # Data transfer objects
├── exceptions/             # Custom exceptions and error handling
├── models/                 # Entity classes
├── repositories/           # Data access interfaces
├── security/               # JWT authentication and security
├── services/               # Business logic
└── utils/                  # Utility classes


## Getting Started

## Prerequisites

-   Java 17 or higher

-   Maven 3.6+

-   PostgreSQL 14+

-   Docker (for running tests with TestContainers)


## Installation

1.  Clone the repository:
```bash 
git clone https://github.com/yourusername/customer-api.git
cd customer-api
```
2. Configure the database connection in  `application.properties`:

```text 
spring.datasource.url=jdbc:postgresql://localhost:5432/customerdb
spring.datasource.username=postgres 
spring.datasource.password=yourpassword
```

3. Configure JWT settings in `application.properties`:

```text 
jwt.secret=your-secret-key-should-be-at-least-32-characters 
jwt.expiration=86400
```

4. Build the application:

```bash 
mvn clean install
```

5.  Modify setup.sh permissions (Optional):

```bash 
chmod +x setup.sh
```

6.  Run setup.sh script to initialize DB:

```bash 
./setup.sh
```

7. Run the application:

```bash 
mvn spring-boot:run
```

## API Endpoints

## Authentication

-   `POST /api/auth/register`: Register a new user

-   `POST /api/auth/login`: Login and get JWT token


## Customers

-   `GET /api/customers`: Get all customers

-   `GET /api/customers/{id}`: Get customer by ID

-   `POST /api/customers`: Create a new customer

-   `PUT /api/customers/{id}`: Update a customer

-   `DELETE /api/customers/{id}`: Delete a customer

-   `GET /api/customers/average-age`: Get average age of all customers

-   `GET /api/customers/age-range?minAge=X&maxAge=Y`: Get customers between specified ages


## API Documentation

Once the application is running, you can access the Swagger UI at:
text

```text
http://localhost:8080/swagger-ui.html
```

## Testing

The application includes comprehensive tests for all components:

-   Unit tests for services, controllers, and utilities

-   Integration tests using TestContainers for PostgreSQL


Run the tests with:


```bash
mvn test
```

## Test Coverage

The project uses JaCoCo for code coverage analysis. Generate a coverage report with:

```bash
mvn verify
```
Then open  `target/site/jacoco/index.html`  in your browser to view the report.

## Security Considerations

-   Passwords are encrypted using BCrypt

-   JWT tokens are used for stateless authentication

-   API endpoints are protected with role-based authorization

-   Strong password validation is enforced during user registration


## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.