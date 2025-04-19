# User Management Service

## Overview

The User Management Service is a Spring Boot-based RESTful service designed to manage user information. It handles user creation, retrieval, and validation while ensuring security and logging.
This service supports the H2 database for demo purposes and can be scaled with Docker and Kubernetes.
Key Features:

* User CRUD operations (Create, Read)
* Validation of user data (name, date of birth)
* Security using Spring Security (JWT authentication)
* Logging with configurable levels for different environments
* Rate Limiting with Bucket4j to control request frequency
* Circuit Breaker with Resilience4j for fault tolerance
* Global Exception Handling for user friendly error messages
* Swagger Documentation for exposed APIs
* Unit Tests for critical components
* Docker & Kubernetes support for containerized deployment
* Environment-based Configuration for different stages of deployment (Dev, Test, Prod)

## Project Structure
```
src/
├── main/
│   ├── java/
│   │   ├── com/
│   │   │   ├── bank/
│   │   │   │   ├── userManagement/
│   │   │   │   │   ├── common/              # Common const for the service
│   │   │   │   │   ├── config/              # Config for rate limiting,security and swagger documentation 
│   │   │   │   │   ├── controller/          # REST controllers for User API
│   │   │   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   │   │   ├── entity/              # JPA entities for database mapping
│   │   │   │   │   ├── exception/           # Global Exception handler and custom exception handling
│   │   │   │   │   ├── filter/              # Request tracking filter to add id to each request
│   │   │   │   │   ├── repository/          # Repository for user table
│   │   │   │   │   ├── service/             # Business logic
│   │   │   │   │   ├── util/                # Utility classes for validation, transformation
│   ├── resources/
│   │   ├── application.properties           # App config (DB, logging, etc.)
│   │   ├── logback.xml                      # Logback config for logging
│   ├── docker/Dockerfile                    # Dockerfile for containerization
│   ├── deployment/deployment.yaml/          # Kubernetes deployment and service YAMLs
└── test/
├── java/
│   ├── com/
│   │   ├── bank/
│   │   │   ├── userManagement/
│   │   │   │   ├── service/                 # Service layer tests
│   │   │   │   ├── controller/              # Controller tests
│   │   │   │   ├── util/                    # Utility tests
```

## File Descriptions 
#### 1. Controller (com.bank.userManagement.controller)

Handles all incoming HTTP requests and returns appropriate responses. This layer interacts with the service layer to fetch or process user data.
* UserController: Defines the endpoints to manage users (/users, /users/{id}).

#### 2. Service (com.bank.userManagement.service)

Contains the business logic for user operations. It communicates with the repository layer to perform database operations.
* UserService: Contains methods like createUser, getUserById, updateUser, deleteUser, etc.

#### 3. Entity (com.bank.userManagement.entity)

Represents the database schema using JPA entities.
* UserEntity: Defines the user model, including fields like id, firstName, lastName, and dateOfBirth.

#### 4. DTO (com.bank.userManagement.dto)

Data Transfer Objects used for input and output data in the API.
* UserDTO: The object passed in API requests and responses.

#### 5. Exception Handling (com.bank.userManagement.exception)
Contains Global Exception handler and custom exceptions for error handling across the service.

#### 6. Utilities (com.bank.userManagement.util)

Contains helper methods for transforming and validating user data.
* ValidationUtil: Validates user details such as name and date of birth.
* TransformationUtil: Converts between UserEntity and UserDTO.

#### 7. Configuration (com.bank.userManagement.config)

Contains configuration classes for various functionalities.
* SecurityConfig: Configures Spring Security for authentication. 
* RateLimitingConfig: Sets up rate limiting using Bucket4j. 
* ResilienceConfig: Configures circuit breakers with Resilience4j.


## Security

The service uses basic authentication to secure all endpoints. Admin credentials are hardcoded as follows:

    Username: admin

    Password: admin123

For actual implementations in production, these credentials should be securely stored in a vault or a credential management system.


## How to Run
Using Maven

1. Clone the repository.
    ``````
    https://github.com/karan-saj/user-management-service.git
    ``````
2. Open a terminal and navigate to the project directory. 
3. Run the following command to build the project:
    ``````
    mvn clean package -DskipTests
    ``````
4. To run the application:
    ``````
    java -jar target/userManagement-0.0.1-SNAPSHOT.jar
    ``````
    The application will be running on http://localhost:8080.

5. Using Docker 
   Build the Docker image:
    ``````
    docker build -t user-management-service .
    ``````
6. Run the Docker container:
    ``````
    docker run -p 8080:8080 user-management-service
    ``````
    The application will be available at http://localhost:8080.

7. Using Kubernetes
   * Build the Docker image and push it to your container registry. 
   * Apply the Kubernetes configurations from the docker/k8s directory:
    ``````
    kubectl apply -f k8s/deployment.yaml
    ``````
    ``````
    kubectl apply -f k8s/service.yaml
    ``````

## Swagger Documentation

The API documentation is automatically generated using Swagger. To view the API documentation:

Open a browser and go to
``````    
http://localhost:8080/swagger-ui.html
``````
This will open a UI where you can explore all available endpoints and try them out directly.

## cURL Examples for Testing

You can use the following cURL commands to test the system:
1. Retrieve User by ID
``````
curl --location 'http://localhost:8080/user/1' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=51F8A1928A07A77CD81F3A7BF04DD584'
``````
2. Create a New User
``````
curl --location 'http://localhost:8080/user' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic YWRtaW46YWRtaW4xMjM=' \
--header 'Cookie: JSESSIONID=51F8A1928A07A77CD81F3A7BF04DD584' \
--data '{
"firstName": "John Max",
"lastName": "Doe",
"dateOfBirth": "2000-01-01"
}'
``````

## Features Implemented

### Security:

    JWT-based authentication using Spring Security.
    Secure endpoints that require authentication (e.g., user creation).

### Logging:

    Configurable logging using Logback for different environments.
    Logs user requests and errors with request-specific identifiers.
    Logs are added to static file or archival

### Rate Limiting:

    Bucket4j is used to prevent abuse by limiting requests from the same IP or user.
    Configured with a sliding window size of 5 requests per 60 seconds.

### Circuit Breaker:

    Resilience4j handles fault tolerance by preventing system overload in case of failure.
    Circuit breaker is set with a failure rate threshold of 50% and 10s open state.

### Global Exception Handling:

    Custom exceptions are thrown and handled globally.
    Clean error messages are returned to the client.

### Unit Tests:

    Service and controller layers have unit tests to validate core functionality.

### Running in Different Environments

    The application supports environment-based configurations using application.properties for various stages like Dev and Prod.
    For example, the H2 console is enabled in development but disabled in production.


## Future Improvements to Make the Service Production Ready

* Database: Replace H2 with a production-grade database like PostgreSQL or MySQL. Add long term storage for archival and seperate storage for analytics and impressions is required.

* Caching: Add caching (e.g., with Redis) to improve read performance for frequently accessed data.

* Monitoring & Alerts: Integrate with Prometheus and Grafana for real-time monitoring. Set up alerting mechanisms for system failures or high load.

* CI/CD: Set up a continuous integration and deployment pipeline with tools like Jenkins or GitLab CI.

* Error Handling: Improve error handling by using Sentry or another error tracking service.

* API Versioning: Implement API versioning to maintain backward compatibility with older clients.

* Scalability: Optimize the application for horizontal scaling and deploy it on a Kubernetes cluster for automatic scaling.

* Compliance & Security: Ensure the service adheres to GDPR and other privacy regulations. Implement role-based access control (RBAC) for more fine-grained security control.
