
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
├── controller/            # REST API endpoints
├── dataTransferObject/    # Data transfer objects
├── exception/             # Custom exceptions and error handling
├── model/                 # Entity classes
├── repository/           # Data access interfaces
├── security/               # JWT authentication and security
├── service/               # Business logic
└── util/                  # Utility classes
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
git clone https://github.com/kprohaszka/customerapi.git
cd customerapi
```
2. Configure the database connection in  `application.properties` (optional, handled in setup.sh):

```text 
spring.datasource.url=jdbc:postgresql://localhost:5432/customerdb
spring.datasource.username=postgres 
spring.datasource.password=yourpassword
```

3. Configure JWT settings in `application.properties` (optional, handled in setup.sh):

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

## Authentication endpoints

This segment provides information on how to use the authentication endpoints for user registration and login.

### Register a new user

This endpoint allows you to create a new user account in the system.

## Endpoint: `POST /api/auth/register`

## Request body:

```json
{
  "username": "johndoe",
  "password": "\"4]C54j,ni$839or",
}
```

### Constraints: 

-   Password must be at least 16 characters, containing upper and lowercase letters, numbers and symbols.
-   
-   An email address can only be used once.

## Successful Response (200 OK) example:

```json
{
  "id": ad1940fc-11a8-4f04-83d2-92f204a4f750,
  "username": "johndoe",
  "email": "john.doe@example.com",
  "password": "$2a$10$zLwB04YeRMV7ypgdIH6Cder0ErtFIPR.aFuM6pDI5sWf2hXrGfH.u",
  "role": null
}
```

### Notes:

-   Passwords are only returned in a hashed version for development purposes and should not be returned like this in production.

-   Role is not filled at the moment but can be used in the future for more precise authentication based on a role level (User, Admin etc.)

## Error Response (400 Bad Request) example:

```json
{
Error during registration: could not
execute statement [ERROR: duplicate key value violates unique constraint "users_username_key"]
}
```

### User Login

This endpoint authenticates a user and returns a JWT token that can be used for subsequent authenticated requests.
## Endpoint: `POST /api/auth/login`
## Request Body:

```json
{
  "username": "johndoe",
  "password": "\"4]C54j,ni$839or"
}

```

## Successful Response (200 OK):

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjE2MTU5MDIyLCJleHAiOjE2MTYxNjI2MjJ9.example_token_signature"
}
```

## Error Response (401 Unauthorized):

```json
{"Invalid username or password"}
```

### Using the JWT Token
After successful authentication, you should:
	1.	Store the JWT token securely (e.g., in localStorage, HttpOnly cookies, etc.)
	2.	Include the token in the Authorization header for subsequent API requests:

 ```json
{
Authorization: Bearer
 eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjE2MTU5MDIyLCJleHAiOjE2MTYxNjI2MjJ9.example_token_signature
}

```

### Implementation Details
The authentication controller uses Spring Security’s `AuthenticationManager` to validate user credentials and a custom `JwtUtil` class to generate JWT tokens.
For registration, the controller delegates to the `UserService` to handle user creation, which includes password encoding and validation.

### Error Handling

-   Registration errors return a 400 Bad Request status with an error message
-   Authentication errors return a 401 Unauthorized status with an error message

### Security Considerations
	•	Using JWT tokens with expiration for secure authentication

### Data Transfer Objects
The API uses the following DTOs:
	1.	LoginRequest: Contains username and password for authentication
	2.	AuthResponse: Contains the JWT token returned after successful authentication

------

## Customer Management Endpoints

This segment provides information on how to use the customer management endpoints for creating, retrieving, updating, and deleting customers, as well as retrieving customer statistics.

### Create a new Customer

## Endpoint: `POST /api/customers`
## Request Body:

```json
{
		"id": "e5e58977-9ae6-4b11-8041-20159856af3f",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "dateOfBirth": "1990-01-15",
  "phoneNumber": "+1234567890",
}
```

## Successful Response (201 Created):

```json
{
	"id": "e5e58977-9ae6-4b11-8041-20159856af3f",
	"firstName": "John",
	"lastName": "Doe",
	"email": "john.dooe@example.com",
	"dateOfBirth": "1990-01-01",
	"phoneNumber": "+1234567890"
}
```

## Error Responses:

-   409 Conflict (Duplicate email):

```json
{
	"error": "Duplicate email",
	"message": "A customer with this email already exists"
}
```

### Get a Customer by ID
This endpoint retrieves a specific customer by their ID.

## Endpoint: `GET /api/customers/{id}`
##Path Parameter:
-   `id`: UUID of the customer to retrieve

## Successful Response (200 OK):

```json
{
	"id": "e5e58977-9ae6-4b11-8041-20159856af3f",
	"firstName": "John",
	"lastName": "Dooe",
	"email": "john.doe@example.com",
	"dateOfBirth": "1990-01-01",
	"phoneNumber": "+1234567890"
}
```

## Error Response (404 Not Found):

```json
{
	"error": "Customer not found",
	"message": "Customer not found with id: 945decf1-6e23-4e9b-a6c8-0f69f1f10422"
}
```

### Get All Customers
This endpoint retrieves a list of all customers.

## Endpoint: `GET /api/customers`

## Successful Response (200 OK):

```json
[
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-15",
    "phoneNumber": "+1234567890",
  },
  {
    "id": "a1b2c3d4-e5f6-4a5b-9c8d-7e6f5a4b3c2d",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "dateOfBirth": "1985-06-22",
    "phoneNumber": "+1987654321",
  }
]
```

### Update a Customer
This endpoint updates an existing customer’s information.

## Endpoint: `PUT /api/customers/{id}`

## Path Parameter:
	•	`id`: UUID of the customer to update

## Request Body

```json
{
	"id": "e5e58977-9ae6-4b11-8041-20159856af3f",
	"firstName": "John",
	"lastName": "Dooe",
	"email": "john.doe@example.com",
	"dateOfBirth": "1990-01-01",
	"phoneNumber": "+1234567890"
}
```

## Successful Response (200 OK):

```json
{
	"id": "e5e58977-9ae6-4b11-8041-20159856af3f",
	"firstName": "John",
	"lastName": "Dooe",
	"email": "john.doe@example.com",
	"dateOfBirth": "1990-01-01",
	"phoneNumber": "+1234567890"
}
```

## Error Responses:

- ## 404 Not Found:

```json
{
  "error": "Customer not found",
  "message": "Customer not found with id: f47ac10b-58cc-4372-a567-0e02b2c3d479"
}
```

- ## 409 Conflict (Duplicate email):


```json
{
  "error": "Duplicate email",
  "message": "A customer with this email already exists"
}
```

### Delete a Customer
This endpoint deletes a customer from the system.

## Endpoint: `DELETE /api/customers/{id}`

## Path Parameter:
-   `id`: UUID of the customer to delete

## Successful Response (204 No Content): 
No response body is returned for a successful deletion.

## Error Response (404 Not Found):

```json
{
	"error": "Customer not found",
	"message": "Customer not found with id: c5539a9d-af41-4bc9-bb70-1bdd4aa446b1"
}
```

### Get Average Customer Age
This endpoint calculates and returns the average age of all customers.

## Endpoint: `GET /api/customers/average-age`

## Successful Response (200 OK):

```json
{
35.7
}
```

## Error Response (500 Internal Server Error):

{
  "error": "Failed to calculate average age",
  "message": "Error retrieving customer data"
}

### Get Customers Within Age Range

## This endpoint retrieves customers whose ages fall within the specified range.

## Endpoint: `GET /api/customers/age-range?minAge=25&maxAge=40`

## Query Parameters:

-   `minAge`: Minimum age (inclusive)
-   `maxAge`: Maximum age (inclusive)
  
## Successful Response (200 OK):

```json
[
  {
    "id": "f47ac10b-58cc-4372-a567-0e02b2c3d479",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "dateOfBirth": "1990-01-15",
    "phoneNumber": "+1234567890",
  },
  {
    "id": "a1b2c3d4-e5f6-4a5b-9c8d-7e6f5a4b3c2d",
    "firstName": "Jane",
    "lastName": "Smith",
    "email": "jane.smith@example.com",
    "dateOfBirth": "1985-06-22",
    "phoneNumber": "+1987654321",
  }
]

```

## Error Responses:
- ## 400 Bad Request (Invalid parameters):

```json
{
  "error": "Invalid parameters",
  "message": "Both minAge and maxAge are required"
}
```

- ## 400 Bad Request (Invalid range):

```json
{
  "error": "Invalid age range",
  "message": "minAge must be less than or equal to maxAge"
}
```

### General Error Handling
The API uses consistent error responses with the following structure:

```json
{
  "error": "Error type",
  "message": "Detailed error message"
}
```

### Authentication
All endpoints in this controller require authentication. Make sure to include the JWT token in the Authorization header:

```json
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huZG9lIiwiaWF0IjoxNjE2MTU5MDIyLCJleHAiOjE2MTYxNjI2MjJ9.example_token_signature
```

### Implementation Details
The customer controller handles CRUD operations for customer management and provides statistical endpoints. 

It uses a `CustomerService` for business logic and includes comprehensive error handling for various scenarios.

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

Errors that may occur and how to handle them:

- **400 Bad Request - Missing Parameter**: This occurs when the `/age-range` endpoint is called without the right parameters. Please make sure you add them as expected.
- **400 Bad Request - Invalid Age Range**: This occurs when the `/age-range` endpoint is called and the minParameter is bigger than the maxParameter. Please make sure you add them as expected.
- **401 Unathorized - Invalid username or password**: As the message says, the password or the username is invalid, please double check you are registered and everything is correct.
- **403 Forbidden**: If you see an error like `403 Forbidden`, ensure that the Authentication bearer token is added to the request and it is a fresh one acquired through to login endpoint. 
- **404 Not Found - Customer Not Found:**: This error can occur with multiple endpoints, it means that the UUID for the given customer does not exists, it is either mistyped or not in the database.
- **409 Conflict - Duplicate Email**: This error can occur when creating or updating a customer with an email that already exists within the database, since it should be unique.
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

While the project is functional, I would like to provide some ways to improve the program, if it ever further develops, as a roadmap, due to the limited development time:

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
-   **Https instead of http**: In production a https certificate would a great step to improve safety and conform modern needs.

By implementing these improvements, the project will become more robust, secure, scalable and clear for new users.

------

## License

This project is licensed under the MIT License - see the LICENSE file for details.

------

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
