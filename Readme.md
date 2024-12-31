# Student Course Registration System

This project is a complete student registration to courses system developed using **Spring Boot**, **Java**, **Maven**, **MySQL**, and **Hibernate (JPA)**. It was developed within 48 hours as part of a sprint challenge to demonstrate skills in database design and backend development.

---

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Documentation](#api-documentation)
- [Database Schema](#database-schema)
- [Postman Collection](#postman-collection)
- [Testing](#testing)
- [Project Structure](#project-structure)
- [Logging](#logging)
- [Error Handling](#error-handling)
- [Future Enhancements](#future-enhancements)
- [Feedback](#feedback)

---

## Features

### Project Features

- **Modular Architecture**: Easily extendable and maintainable codebase.
- **Input Validation**: Ensures data integrity and prevents invalid data entry.
- **RBAC (Role-Based Access Control)**: Fine-grained user permissions for Admins and Students.
- **Data Security**: Using BCrypt for storing hashed passwords and prevent logging of sensitive data.
- **ORM Integration**: Simplified database interaction using Hibernate (JPA).
- **Database Migration with Flyway**: Automatic database schema creation and version control with pre-seeded default values.
- **Logging**: Comprehensive logging for debugging and monitoring.
- **Error Handling**: Graceful management of runtime errors and exceptions.

### Admin Features

- **Authentication**: Admins can log in using their email and password.
- **CRUD Operations on Students**: Create, read, update, and delete student records.
    - When creating a new student, an auto-generated password is assigned for student login.
- **CRUD Operations on Courses**: Create, read, update, and delete course records.
- **View All Students**: Fetch all students along with the list of courses they are registered for.
- **View All Courses**: Fetch all courses along with the list of students registered in each course.

### Student Features

- **Authentication**: Students log in using their email and password.
- **Register for Courses**:
    - Students can register for up to **2 courses**.
    - A course cannot have more than **30 students**.
- **Cancel Registration**: Students can cancel their registration for a course.
- **View Registered Courses**: Students can view the list of courses they are registered for.

---

## Architecture

- **Backend**: Spring Boot application providing RESTful APIs.
- **Database**: MySQL database managing relational data.
- **ORM**: Hibernate (JPA) for database interaction.
- **Authentication**: Session-based authentication with session keys.
- **API Documentation**: Swagger/OpenAPI for documenting APIs.

---

## Prerequisites

- **Java 23** or higher (Built with Azul JDK: azul-23.0.1)
- **Maven 3.9.9** or higher
- **MySQL 8.0** or higher
- **Git** (to clone the repository)

---

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/elchananvol/student-course-registration-system.git
   cd student-course-registration-system
   ```

2. **Set Up the Database**

    - Ensure MySQL is installed and running.
    - Create a new database (the schema will be managed by Flyway migrations).

   ```sql
   CREATE DATABASE registration;
   ```

    Configure the application by updating the `src/main/resources/application.yml` file with your database credentials:
    
    ```yaml
    spring:
      datasource:
        url: jdbc:mysql://localhost:3306/registration
        username: your_db_username
        password: your_db_password
    ```
   
---

## Running the Application

You can run the application using Maven:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8090`.

---

## API Documentation

API documentation is available via Swagger UI once the application is running:

- **Local Swagger UI**: [http://localhost:8090/swagger-ui.html](http://localhost:8080/swagger-ui.html)

Alternatively, you can view the API documentation on SwaggerHub:

- **SwaggerHub API Documentation**: [Student Course Registration System API](https://app.swaggerhub.com/apis/ElchananVol/StudentCourseRegistrationSystem/1.0.0)

---

## Database Schema

The database schema is managed using Flyway migrations.
<br>Information about the schema are in the [design file](docs/system%20design.md#412-database-schema).
<br>The database schema is managed using Flyway migrations.
<br>The SQL scripts are located in the `src/main/resources/db/migration` folder.

- **Initial Schema Creation**: [V1__Create_tables.sql](src/main/resources/db/migration/V1__Create_tables.sql)
- **Sample Data Insertion**: [V2__Insert_initial_values.sql](src/main/resources/db/migration/V2__Insert_initial_values.sql)

---

## Testing

### Default Users

**Admin User**:

- **Email**: `test_admin`
- **Password**: `1234`

**Student Users**:

- **Email**: `test_student`
- **Password**: `1234`
- 
### Using Postman

A Postman collection with all API endpoints is provided for easy testing:

- [Student Course Registration System API.postman_collection.json](docs/Student%20Course%20Registration%20System%20API.postman_collection.json)

Import this file into Postman to quickly test all the API endpoints.

- Import the Postman collection.
- Use the **Login** endpoints to authenticate as Admin or Student.
- The session key will be extracted by the post-request script and automatically configured for subsequent API calls.

---

## Project Structure

```
student-course-registration-system/
├── src/
│   ├── main/
│   │   ├── java/com/example/registration/
│   │   │   ├── controller/     // REST Controllers
│   │   │   ├── model/          // Entity Models
│   │   │   ├── repository/     // JPA Repositories
│   │   │   ├── service/        // Service Layer
│   │   │   ├── security/       // Security configuration implementation
│   │   │   ├── exceptions/     // Custom exception module
│   │   │   └── SystemApplication.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── openapi.yaml
│   │       └── db/
│   │           └── migration/  // Flyway SQL Scripts
│   └── test/
├── docs/                       // Documentation and Postman Collection
├── pom.xml
└── README.md
```

---

## Logging

- Logging is configured using SLF4J with Logback.
- Logs capture key events, errors, and data processing steps.
- Log files are stored in the `logs` directory.

---

## Error Handling

- **Validation Errors**: Return HTTP 400 Bad Request with error details.
- **Authentication Errors**: Return HTTP 401 Unauthorized.
- **Authorization Errors**: Return HTTP 403 Forbidden.
- **Not Found Errors**: Return HTTP 404 Not Found.
- **Constraint Violations**: Return appropriate error messages when business constraints are violated (e.g., student tries to register for more than 2 courses).

---

## Future Enhancements

- **Frontend Application**: Develop a React frontend for improved user experience.
- **Dockerization**: Create Dockerfiles to containerize the application.
- **CI/CD Pipeline**: Implement continuous integration and deployment.
- **Unit and Integration Testing**: Add comprehensive test cases.

---

## Feedback

This system was designed to demonstrate scalability, maintainability, and best practices in a short timeframe. Your feedback is welcome!

---

Feel free to reach out via [elchanan.vol@gmail.com](mailto:elchanan.vol@gmail.com) for any questions or suggestions.
