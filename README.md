
# Customer API

A RESTful API for managing customer data with JWT authentication.

## Overview

Customer API is a Spring Boot application that provides a secure way to manage customer information. It includes features for user authentication, customer CRUD operations, and age-based analytics.

------

## Features

-   **User Authentication**: Secure JWT-based authentication

-   **Customer Management**: Create, read, update, and delete customer records

-   **Data Validation**: Comprehensive input validation for customer data

-   **Age Analytics**: Calculate average customer age and filter customers by age range

-   **API Documentation**: Swagger UI for easy API exploration

-   **Security**: Role-based access control and password encryption

------

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

```
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
```

## Getting Started

## Prerequisites

-   Java 17 or higher

-   Maven 3.6+

-   PostgreSQL 14+

-   Docker (for running tests with TestContainers)

------

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

------

## Customers

-   `GET /api/customers`: Get all customers

-   `GET /api/customers/{id}`: Get customer by ID

-   `POST /api/customers`: Create a new customer

-   `PUT /api/customers/{id}`: Update a customer

-   `DELETE /api/customers/{id}`: Delete a customer

-   `GET /api/customers/average-age`: Get average age of all customers

-   `GET /api/customers/age-range?minAge=X&maxAge=Y`: Get customers between specified ages

------

## API Documentation

Once the application is running, you can access the Swagger UI at:
text

```text
http://localhost:8080/swagger-ui.html
```

------

## Testing

The application includes comprehensive tests for all components:

-   Unit tests for services, controllers, and utilities

-   Integration tests using TestContainers for PostgreSQL


Run the tests with:


```bash
mvn test
```
------

## Test Coverage

The project uses JaCoCo for code coverage analysis. Generate a coverage report with:

```bash
mvn verify
```
Then open  `target/site/jacoco/index.html`  in your browser to view the report.

------

## Security Considerations

-   Passwords are encrypted using BCrypt

-   JWT tokens are used for stateless authentication

-   API endpoints are protected with role-based authorization

-   Strong password validation is enforced during user registration

------

## Troubleshooting

- **400 Bad Request - Missing Parameter**: This occurs when the `/age-range` endpoint is called without the right parameters. Please make sure you add them as expected.
- **400 Bad Request - Invalid Age Range**: This occurs when the `/age-range` endpoint is called and the minParameter is bigger than the maxParameter. Please make sure you add them as expected.
- **401 Unathorized - Invalid username or password**: As the message says, the password or the username is invalid, please double check you are registered and everything is correct.
- **403 Forbidden**: If you see an error like `403 Forbidden`, ensure that the Authentication bearer token is added to the request and it is a fresh one acquired through to login endpoint. 
- **404 Not Found - Customer Not Found:**: This error can occur with multiple endpoints, it means that the UUID for the given customer does not exists, it is either mistyped or not in the database.
- **500 Internal Server Error:**: This may happen when a new customer is created with an email that already exists in the database.
 ------

## Development Notes

Some of the reasons behind implementing parts in a certain way:

- **UUID instead of Long with Auto-Increment for CustomerId in DB**: While it may be more comfortable at first, the issue here is not what many would think, that it would run out of ids, as it would take years and years in a normal scenario, but the benefit is more in debugging / spotting errors. With Long, if there is an error in a query when joining multiple tables, it would be hard to see the errors. The same error with UUIDs probably would result nothing, or nulls, so it would be easier to spot the mistake. With sequential ints, there is the possibility of not realizing a mistake, which can be problematic on the long run.


------

## Known-issues

There are some known issues that can be fixed later on for a more robust software:

-   **JWT Refreshing Functionality**: Add and endpoint which would serve to refresh the JWT token before it expires.
-   **Adding a UI**: Eventually a UI can be added to make this program more user friendly
-   **Migrating to a different SQL Dialect**: There is a possibility that not every dialect is supported due to the current structure.
------

## Improvements & Future Enhancements

While the project is functional, I would like to provide some ways to improve the program:

-   **Enhanced Logging**: Extend logging coverage for a more robust program.
-   **Error Handling**: Implement wider error handling to capture and report issues more effectively.
-   **Rate Limiting**: Apply rate limiting middleware to prevent abuse.
-   **Automated CI/CD**: Set up GitHub Actions or another CI/CD pipeline to automate testing and deployment.
-   **More accurate responses**: The response messages could be tailored even better with more detail.
-   **Adding a UI**: Eventually a UI can be added to make this program more user friendly
-   **JWT Refreshing Functionality**: Add and endpoint which would serve to refresh the JWT token before it expires.
-   **Adding a UI**: Eventually a UI can be added to further develop this program, or it can be used to integrate with different programs that need CRUD functionality.
-   **Run testcontainers only when needed**: Currently testcontainers are created for each class, however it would be wiser to only use it in tests where it is absolutely neccessary.
-   **Use a query for getCustomersBetweenAges**: Currently this method uses streaming for calculation due to the requirements, and it is getting every customer with .findAll(), however this is slower, and a query would be a much better solution here.
-   **Use a native query for getAverageAge**: At the moment it uses a JPQL query due on the requirements, however a native query would make more sense, would be more robust and precise.
-   **Using kubernetes secrets or similar**: Right now, the software stores the DB credentials locally, without uploading it (.gitignored). However, there is a more elegant way doing this, for example with kubernetes secrets or similar, or storing them in the Secret or ConfigMap then mounting it to the container as a file.
-   **Double check native SQLs, scalability and modularity**: The application is built with modularity and scalability in mind, so each part should ensure it can be easily migrated to other dialects, databases etc.
-   **Change 500 Internal Server Error to Email Already Exists**: Currently this is not handled with a proper error code.

By implementing these improvements, the project will become more robust, secure, scalable and clear for new users.

------

## License

This project is licensed under the MIT License - see the LICENSE file for details.

------

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
