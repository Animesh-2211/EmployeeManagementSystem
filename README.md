# Employee Management System

This is a Spring Boot-based Employee Management System that handles employee registration, login, CRUD operations, session management, and authorization using custom session-based authentication.

## Features
- **Employee Registration**: Users can register with their details.
- **Employee Login**: Users can log in with their email and password.
- **Session Management**: Generates and manages session IDs for logged-in users.
- **CRUD Operations**: Allows creation, reading, updating, and deletion of employee data.
- **Search Functionality**: Search employees by id.
- **Pagination and Sorting**: Provides paginated and sorted data for employee listings.
- **Authorization Filter**: Protects endpoints using session-based authorization.

## Endpoints

### Public Endpoints (No Authorization Required)
- **POST /employees/register**: Register a new employee.
- **POST /employees/login**: Log in an employee and return a session ID.

### Protected Endpoints (Session ID Required)
- **GET /employees**: Get a paginated list of all employees.
- **GET /employees/{id}**: Get details of a specific employee.
- **PUT /employees/{id}**: Update an employee's details.
- **DELETE /employees/{id}**: Delete an employee.

## Session Management
The client must include a `sessionId` in the request headers for all protected endpoints.
Example header: `sessionId: <session-id>`.

### How Session Management Works
- Upon login, a session ID is generated for the employee and sent back to the client.
- The session ID must be included in the headers for all requests to protected endpoints.
- The custom `SessionFilter` intercepts the request, checks the validity of the session ID, and either allows or blocks the request.

## Code Breakdown
- **EmployeeService**: Handles the business logic for registration, login, employee CRUD operations, and search.
- **SessionService**: Manages session creation, retrieval, and invalidation.
- **SessionFilter**: A custom filter that checks if requests contain a valid session ID, blocking unauthorized access.
- **EmployeeController**: Defines the REST API endpoints and interacts with the service layer.
- **EmployeeRepository**: Interface for interacting with the database using Spring Data JPA.

## THANKYOU
