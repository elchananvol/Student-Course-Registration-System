# System Design Document

## Project Title: Student Course Registration System

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [System Overview](#2-system-overview)
    - [2.1 High-Level Architecture](#21-high-level-architecture)
3. [Design Considerations](#3-design-considerations)
    - [3.1 Technology Stack](#31-technology-stack)
    - [3.2 Design Patterns](#32-design-patterns)
    - [3.3 Security](#33-security)
    - [3.4 Scalability and Performance](#34-scalability-and-performance)
4. [Detailed Design](#4-detailed-design)
    - [4.1 Database Design](#41-database-design)
        - [4.1.1 Entity-Relationship Diagram](#411-entity-relationship-diagram)
        - [4.1.2 Database Schema](#412-database-schema)
    - [4.2 Backend Design](#42-backend-design)
        - [4.2.1 REST API Endpoints](#421-rest-api-endpoints)
        - [4.2.2 Service Layer](#422-service-layer)
        - [4.2.3 Data Access Layer](#423-data-access-layer)
        - [4.2.4 Exception Handling](#424-exception-handling)
        - [4.2.5 Logging](#425-logging)
5. [Authentication and Authorization](#5-authentication-and-authorization)
    - [5.1 Admin Authentication](#51-admin-authentication)
    - [5.2 Student Authentication](#52-student-authentication)
    - [5.3 Session Management](#53-session-management)
6. [Non-Functional Requirements](#6-non-functional-requirements)
    - [6.1 Performance](#61-performance)
    - [6.2 Security](#62-security)
    - [6.3 Reliability](#63-reliability)
    - [6.4 Maintainability](#64-maintainability)
7. [Deployment Architecture](#7-deployment-architecture)
    - [7.1 Environment Setup](#71-environment-setup)
    - [7.2 Continuous Integration/Continuous Deployment (CI/CD)](#72-continuous-integrationcontinuous-deployment-cicd)
8. [Conclusion](#8-conclusion)
9. [References](#9-references)

---

## 1. Introduction

This document outlines the comprehensive system design for the **Student Course Registration System**, a web application developed using Java Spring Boot and MySQL. The system facilitates administrators in managing student and course records and enables students to register for courses within defined constraints.

---

## 2. System Overview

The system is designed to provide a robust, scalable, and secure platform for course registration and management. It comprises:

- **Backend:** RESTful APIs developed using Java Spring Boot.
- **Database:** MySQL relational database to store and manage data.
- **Testing Tools:** Postman for API testing.

### 2.1 High-Level Architecture

![High-Level Architecture Diagram](https://i.imgur.com/YourImageLink.png) *(Note: Replace with an actual diagram)*

- **Client Layer:** React frontend (optional), Postman, or any HTTP client.
- **API Layer:** Controllers handling HTTP requests and responses.
- **Service Layer:** Business logic implementation.
- **Data Access Layer:** Repositories interfacing with the database.
- **Database Layer:** MySQL database hosting the data.

---

## 3. Design Considerations

### 3.1 Technology Stack

- **Backend:**
    - **Language:** Java 18.
    - **Frameworks:** Spring Boot, Spring MVC, Spring Data JPA, Hibernate.
    - **Build Tool:** Maven, OpenAPI, Flyway.
- **Database:** MySQL 8.0+ , RDS.
- **Version Control:** Git, hosted on GitHub.
- **Testing Tools:** Postmen collection (Future Enhancement: JUnit, Mockito).
- **API Documentation:** OpenAPI/Swagger.
- **Logging Framework:** SLF4J with Logback implementation.

### 3.2 Design Patterns

- **MVC (Model-View-Controller):** Separates application into controller, service, and repository layers.
- **DAO (Data Access Object):** Abstracts and encapsulates all access to the data source.
- **Singleton:** For managing shared resources like application configurations.
- **Factory:** For creating instances based on input parameters (e.g., creating different types of error responses).

### 3.3 Security

- **Authentication:** JWT (JSON Web Tokens) for session management.
- **Password Hashing:** BCrypt for storing hashed passwords.
- **Input Validation:** Prevent SQL Injection and Cross-Site Scripting (XSS).
- **HTTPS:** Secure communication (assumed in production deployment).

### 3.4 Scalability and Performance

- **Connection Pooling:** Efficiently manage database connections.
- **Caching (Future Enhancement):** Use Redis or Ehcache for frequently accessed data.
- **Asynchronous Processing:** For long-running tasks (not required in current scope).

---

## 4. Detailed Design

### 4.1 Database Design

#### 4.1.1 Entity-Relationship Diagram

(Future Enhancement) ER Diagram

#### 4.1.2 Database Schema

**Tables:**

1. **Admin**

   | Column        | Data Type     | Constraints                |
      |---------------|---------------|----------------------------|
   | id            | INT           | PK, Auto-increment         |
   | name          | VARCHAR(255)  | NOT NULL                   |
   | email         | VARCHAR(255)  | NOT NULL, UNIQUE           |
   | password_hash | VARCHAR(255)  | NOT NULL                   |

2. **Student**

   | Column        | Data Type     | Constraints          |
   |---------------|---------------|----------------------|
   | id            | INT           | PK, Auto-increment   |
   | name          | VARCHAR(255)  | NOT NULL             |
   | email         | VARCHAR(255)  | NOT NULL, UNIQUE     |
   | password_hash | VARCHAR(255)  | NOT NULL             |

3. **Course**

   | Column      | Data Type     | Constraints                |
      |-------------|---------------|----------------------------|
   | id          | INT           | PK, Auto-increment         |
   | name        | VARCHAR(255)  | NOT NULL                   |
   | description | TEXT          |                            |

4. **Student_Course**

   | Column      | Data Type     | Constraints                              |
   |-------------|---------------|------------------------------------------|
   | student_id  | INT           | PK, FK to Student(id),ON DELETE CASCADE  |
   | course_id   | INT           | PK, FK to Course(id),ON DELETE CASCADE   |

**Constraints and Indexes:**

- **Student_Course PK:** Composite primary key on (`student_id`, `course_id`).
- **Foreign Keys:**
    - `student_id` references `Student(id)`.
    - `course_id` references `Course(id)`.
- **Indexes:**
    - Indexes on `email` columns for quick searches.
    - Indexes on the foreign keys in `Student_Course`.

### 4.2 Backend Design

#### 4.2.1 REST API Endpoints

All endpoints are designed to be consumed by the React frontend and can also be tested using Postman.
updated endpoints are here with **SwaggerHub API Documentation**: [Student Course Registration System API](https://app.swaggerhub.com/apis/ElchananVol/StudentCourseRegistrationSystem/1.0.0)
**Admin Endpoints:**

1. **Authentication:**

    - **Login:**
        - `POST /api/login/admin`
            - **Description:** Authenticate admin and provide JWT session token.
            - **Request Body:**
              ```json
              {
                "email": "admin@example.com",
                "password": "password123"
              }
              ```
            - **Response:**
              ```json
              {
                "token": "jwt_token_string",
                "role": "admin"
              }
              ```

2. **Student Management:**

    - **Create Student:**
        - `POST /api/admin/students`
            - **Description:** Create a new student.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Request Body:**
              ```json
              {
                "name": "John Doe",
                "email": "john.doe@example.com"
              }
              ```
            - **Response:**
              ```json
              {
                "id": 1,
                "name": "John Doe",
                "email": "john.doe@example.com",
                "password": "generated_special_key"
              }
              ```

    - **Retrieve All Students:**
        - `GET /api/admin/students`
            - **Description:** Retrieve all students with their registered courses.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

    - **Retrieve Student by ID:**
        - `GET /api/admin/students/{id}`
            - **Description:** Retrieve a specific student by ID.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

    - **Update Student:**
        - `PUT /api/admin/students/{id}`
            - **Description:** Update student information.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Request Body:**
              ```json
                  {
                  "name": "John Doe",
                  "email": "john.doe@example.com",
                  "password": "12323" 
                  }
              ```
            - **Response:**
                ```json
                {
                  "id": 1,
                  "name": "John Doe",
                  "email": "john.doe@example.com",
                  "password": "generated_special_key"
                }
                ```
              
    - **Delete Student:**
        - `DELETE /api/admin/students/{id}`
            - **Description:** Delete a student.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

3. **Course Management:**

    - **Create Course:**
        - `POST /api/admin/courses`
            - **Description:** Create a new course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Request Body:**
              ```json
              {
                "name": "Mathematics 101",
                "description": "Introductory mathematics course"
              }
              ```
            - **Response:**
                ```json
                {
                  "id": 1,
                  "name": "Mathematics 101",
                  "description": "Introductory mathematics course"
                }
                ```

    - **Retrieve All Courses:**
        - `GET /api/admin/courses`
            - **Description:** Retrieve all courses with enrolled students.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Response:**
                ```json
                {
                  "id": 1,
                  "name": "Mathematics 101",
                  "description": "Introductory mathematics course"
                }
                ```

    - **Retrieve Course by ID:**
        - `GET /api/admin/courses/{id}`
            - **Description:** Retrieve a specific course by ID.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

    - **Update Course:**
        - `PUT /api/admin/courses/{id}`
            - **Description:** Update course information.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Request Body:** *(Fields to update)*

    - **Delete Course:**
        - `DELETE /api/admin/courses/{id}`
            - **Description:** Delete a course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

4. **Student Course Registration Management:**
    - **Register Student to Course:**
        - `POST /api/admin/students/{student_id}/courses/{course_id}/register`
            - **Description:** Admin registers a student for a course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Constraints:** Validate course capacity and student's course limit.

    - **Cancel Student's Course Registration:**
        - `DELETE /api/admin/students/{student_id}/courses/{course_id}/cancel`
            - **Description:** Admin cancels a student's registration for a course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

**Student Endpoints:**

1. **Authentication:**

    - **Login:**
        - `POST /api/login/student`
            - **Description:** Authenticate student using `special_key`.
            - **Request Body:**
              ```json
              {
                "specialKey": "abc123def456"
              }
              ```
            - **Response:**
              ```json
              {
                "token": "jwt_token_string",
                "studentId": 1,
                "name": "John Doe"
              }
              ```

2. **Course Registration:**

    - **Register for a Course:**
        - `POST /api/students/courses/{course_id}`
            - **Description:** Student registers for a course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`
            - **Constraints:** Validate course capacity and student's course limit.

    - **Cancel Course Registration:**
        - `DELETE /api/students/courses/{course_id}`
            - **Description:** Student cancels their registration for a course.
            - **Request Header:** `Authorization: Bearer {jwt_token}`

3. **Retrieve Registered Courses:**
    - `GET /api/students/courses`
        - **Description:** Retrieve courses the student is registered for.
        - **Request Header:** `Authorization: Bearer {jwt_token}`

#### 4.2.2 Service Layer

- **AdminService:** Manages admin functionalities, including student and course management, and now includes methods for registering/canceling student course registrations.
- **StudentService:** Manages student functionalities.
- **CourseService:** Manages course-related operations.

#### 4.2.3 Data Access Layer

- **Repositories:**
    - **AdminRepository**
    - **StudentRepository**
    - **CourseRepository**

- Utilize Spring Data JPA for ORM capabilities.

#### 4.2.4 Exception Handling

- **Global Exception Handler (@ControllerAdvice):**
    - Handles exceptions and returns standardized error responses.
- **Custom Exceptions:**
    - `ResourceNotFoundException`
    - `UnauthorizedAccessException`
    - `CourseRegistrationException`
    - `StudentRegistrationException`

#### 4.2.5 Logging

- **Logging Framework:** SLF4J with Logback implementation.
- **Log Contents:**
    - Timestamps, log level, class/method names, messages.
- **Sensitive Data:** Ensure no sensitive data is logged.
- **Configuration:** Logging configurations are managed in `logback.xml` or `application.properties`.

---

## 5. Authentication and Authorization

### 5.1 Admin Authentication

- **Login Process:**
    - Admin submits email and password.
    - Backend verifies credentials.
    - On success, generates JWT token and returns it to the frontend.

- **Password Security:**
    - Store hashed passwords using BCrypt.
    - Implement salting.

### 5.2 Student Authentication

- **Login Process:**
    - Student submits email and `special_key`.
    - Backend verifies the key.
    - On success, generates JWT token and returns it to the frontend.

### 5.3 Session Management

- **JWT Tokens:**
    - Include user role (admin/student) in token claims.
    - Set appropriate expiration time (60 minutes).

- **Token Storage:**
    - Store JWT tokens securely in the browser (e.g., HttpOnly cookies or localStorage).

- **Token Validation:**
    - Validate token on each request.
    - Implement middleware/filters in the backend to check the `Authorization` header.

- **Role-Based Access Control (RBAC):**
    - Protect endpoints based on user roles.
    - Ensure that only admins can access admin endpoints, and students can access student endpoints.

- **Logout:**
    - Client-side token invalidation.
    - (Optional) Implement token blacklist on the server.

---

## 6. Non-Functional Requirements

### 6.1 Performance

- **API Response Time:**
    - Target under 500ms for standard operations.
- **Database Optimization:**
    - Use indexing on frequently queried columns.
    - Optimize SQL queries generated by Hibernate.

### 6.2 Security

- **Input Validation:**
    - Sanitize all inputs in both frontend and backend.
- **Error Handling:**
    - Do not expose stack traces or sensitive information to clients.
- **Secure Communication:**
    - Use HTTPS for communication between the frontend and backend (assumed in production).
- **Cross-Origin Resource Sharing (CORS):**
    - Configure appropriate CORS policies to allow frontend to communicate with the backend.

### 6.3 Reliability

- **Transaction Management:**
    - Ensure atomicity for operations involving multiple database changes (e.g., registering for a course).
- **Concurrency Control:**
    - Handle concurrent registrations to prevent exceeding course capacity.

### 6.4 Maintainability

- **Code Organization:**
    - Follow a modular structure with clear separation of concerns.
- **Documentation:**
    - Use Javadoc for backend methods and comprehensive comments in the code.
    - Document frontend components and state management logic.
- **Version Control Practices:**
    - Use feature branching, pull requests, and code reviews.

---

## 7. Deployment Architecture

### 7.1 Environment Setup

- **Development Environment:**
    - **Backend:**
        - Java 23 JDK installed.
        - Maven configured.

- **Testing Environment:**
    - Separate environment mimicking production for QA.

- **Production Environment:**
    - Deployment on cloud services (e.g., AWS EC2, Heroku).
    - Configure environment variables for sensitive data (e.g., database credentials, JWT secret).

### 7.2 Continuous Integration/Continuous Deployment (CI/CD)

- **CI Tools:**
    - Use GitHub Actions or Jenkins for automated builds and tests.

- **Automated Testing:**
    - Run unit tests and integration tests on each commit or pull request.
    - Implement frontend tests using Jest and React Testing Library.

- **Deployment Automation:**
    - Scripts for deploying to testing and production environments.

---

## 8. Conclusion

This updated system design document incorporates the new requirements specified, enhancing the **Student Course Registration System** to include:

- A React frontend for improved user experience.
- Additional admin functionalities to manage student course registrations.
- Detailed endpoint designs to support frontend interactions.
- Updated technology stack embracing Java 17, Maven, and SLF4J for logging.

By adhering to the design specifications and best practices outlined, the system will meet both the functional and non-functional requirements effectively.

---

## 9. References

- **Spring Boot Documentation:** [https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot)
- **MySQL Documentation:** [https://dev.mysql.com/doc/](https://dev.mysql.com/doc/)
- **React Documentation:** [https://reactjs.org/docs/getting-started.html](https://reactjs.org/docs/getting-started.html)
- **SLF4J Documentation:** [http://www.slf4j.org/](http://www.slf4j.org/)
- **JWT Introduction:** [https://jwt.io/introduction](https://jwt.io/introduction)
- **RESTful API Design:** [https://restfulapi.net/](https://restfulapi.net/)
- **Maven Documentation:** [https://maven.apache.org/guides/index.html](https://maven.apache.org/guides/index.html)

---



*This system design document is intended to guide the development team through the implementation process, ensuring all requirements are met and best practices are followed.*