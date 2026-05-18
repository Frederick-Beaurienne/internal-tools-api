# Internal Tools API

Spring Boot REST API for managing internal SaaS tools with analytics, reporting and cost optimization features.

---

# Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Architecture](#project-architecture)
- [Prerequisites](#prerequisites)
- [Clone Repository](#clone-repository)
- [Database Setup](#database-setup)
- [Run the Application](#run-the-application)
- [Swagger Documentation](#swagger-documentation)
- [API Response Structure](#api-response-structure)
- [Testing](#testing)
- [Current Progress](#current-progress)
- [Design Choices](#design-choices)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

# Features

- REST API for internal tools management
- Dynamic multi-criteria filtering
- Case-insensitive search support
- REST-oriented API response contracts
- Structured pagination and sorting metadata
- Centralized structured error responses
- PostgreSQL integration
- PostgreSQL native enum mapping
- Dynamic query building using Criteria API
- OpenAPI / Swagger documentation
- Validation & centralized error handling
- Structured application logging
- Transactional service layer
- REST controller testing
- Integration testing
- Full CRUD REST endpoints
- Dockerized database environment
- Layered architecture

---

# Tech Stack

| Technology | Usage |
|---|---|
| Java 17 | Backend language |
| Spring Boot | REST API framework |
| Spring Data JPA | ORM / persistence |
| PostgreSQL | Database |
| Docker Compose | Local infrastructure |
| SpringDoc OpenAPI | API documentation |
| Maven | Dependency management |
| SLF4J / Logback | Logging |
| JUnit 5 | Testing |
| MockMvc | API testing |

---

# Project Architecture

```text
src/main/java/com/techcorp/internaltoolsapi
├── analytics
│   ├── dto
│   └── service
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
│   └── enums
├── exception
├── mapper
├── repository
├── service
│   └── impl
└── validation
```

The application follows a layered architecture inspired by SOLID principles and common Spring Boot enterprise practices.

---

# Prerequisites

- Java 17+
- Maven 3.9+
- Docker Desktop
- Git

---

# Clone Repository

```bash
git clone https://github.com/Frederick-Beaurienne/internal-tools-api.git

cd internal-tools-api
```

---

# Database Setup

The project includes a ready-to-use PostgreSQL environment.

## Start PostgreSQL

```bash
cd docker/database
docker compose --profile postgres up -d
```

## PostgreSQL Access

| Service | URL / Port |
|---|---|
| PostgreSQL | localhost:5432 |
| pgAdmin | http://localhost:8081 |

Credentials:

```text
username: dev
password: dev123
database: internal_tools
```

## Optional Database Administration Interface

pgAdmin is available for database exploration and debugging:

```text
http://localhost:8081
```

---

# Run the Application

## Return to project root

If you are still in the `docker/database` directory:

```bash
cd ../..
```

## Start Spring Boot application

```bash
mvn spring-boot:run
```

---

# Swagger Documentation

The API uses snake_case JSON naming conventions
for request and response payload consistency.

The API follows REST-oriented response semantics:
- resource payloads are returned directly
- validation and technical errors use structured error payloads
- DELETE operations return HTTP 204 No Content

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

# API Response Structure

## Collection Responses

Collection endpoints return:
- data payload
- applied filters metadata
- pagination metadata
- sorting metadata

Example:

```json
{
  "data": [],
  "total": 20,
  "filtered": 15,
  "filters_applied": {},
  "pagination": {
    "current_page": 0,
    "page_size": 10,
    "total_pages": 2,
    "first": true,
    "last": false
  },
  "sorting": {
    "sort_by": "createdAt",
    "sort_direction": "desc"
  }
}
```

---

## Error Responses

Validation, business and technical errors
return structured error payloads.

Example:

```json
{
  "error": "Validation failed",
  "message": "Invalid request parameters",
  "details": {},
  "timestamp": "2025-08-20T14:30:00Z"
}

---

# Testing

## Run all tests

```bash
mvn test
```

## Current Test Coverage

The project currently includes:

- REST controller tests using `@WebMvcTest`
- Integration tests using `@SpringBootTest`
- Validation and exception handling tests
- CRUD endpoint workflow testing
- PostgreSQL enum integration testing
- API response contract verification
- Dynamic filtering endpoint testing
- Query parameter binding verification

---

# Current Progress

## Completed

- Spring Boot initialization
- PostgreSQL Docker environment
- Database connectivity
- Base project architecture
- Swagger/OpenAPI integration
- Centralized exception handling
- Structured logging system
- JPA entity mapping
- PostgreSQL native enum integration
- DTO / mapper architecture
- Service and repository layers
- Transactional business services
- Full CRUD REST endpoints
- REST controller tests
- Integration tests
- Dynamic filtering system
- Criteria API query specifications
- Combined dynamic filtering
- Case-insensitive filtering
- Pagination support
- Dynamic sorting support
- Structured pagination metadata
- Structured sorting metadata
- REST-oriented response contracts
- Structured error response payloads
- Pagination and sorting response testing
- Structured error response validation
- REST contract verification

## In Progress

- Analytics endpoints

---

# Design Choices

## PostgreSQL

Chosen for:
- strong analytical capabilities
- robust aggregation support
- enterprise-grade reliability

## Layered Architecture

The application follows a layered architecture inspired by SOLID principles and common Spring Boot enterprise practices.

### Main Layers

- **Controller**
  Handles HTTP requests and exposes REST endpoints.

- **Service**
  Contains business logic and application rules.
  Service interfaces are used to improve decoupling and maintainability.

- **Repository**
  Handles persistence operations using Spring Data JPA.

- **DTO**
  Separates API contracts from persistence entities.

- **Mapper**
  Centralizes entity / DTO transformations.

- **Exception Handling**
  Centralized exception management using `@RestControllerAdvice`.

- **Logging**
  Structured application logging using SLF4J and Logback.

## PostgreSQL Enum Mapping

PostgreSQL native ENUM types are mapped directly using Hibernate named enum support.

Java enum values intentionally match PostgreSQL enum values exactly to:
- preserve native PostgreSQL enum support
- avoid unnecessary conversion layers
- simplify ORM persistence
- maintain API and database consistency

Hibernate named enum mapping is configured using:

```java
@JdbcTypeCode(SqlTypes.NAMED_ENUM)
```

## Hibernate Validation Mode

`ddl-auto=validate` is intentionally used to:
- validate schema consistency
- avoid accidental schema mutations
- preserve provided database structure

## Explicit Java Architecture

The project intentionally avoids excessive code generation tools such as Lombok in favor of:
- explicit constructors
- getters/setters
- readable object structure
- maintainable enterprise-style Java code

## Transaction Management

Business write operations are handled within transactional service methods using:

```java
@Transactional
```

## Dynamic Filtering Strategy

Dynamic filtering is implemented using Spring Data JPA Specifications
and the Criteria API.

This approach was chosen to:
- avoid fragile JPQL string concatenation
- support optional combinable filters
- preserve SQL injection protection through parameter binding
- improve maintainability and scalability of query logic

Current filtering capabilities include:
- department
- status
- category
- vendor
- partial name search
- monthly cost range filtering

Filtering behavior:
- exact enum matching
- case-insensitive text filtering
- partial matching for tool names
- inclusive numeric range filtering

Pagination and sorting support are implemented
using Spring Data Pageable and Sort abstractions.

Sorting fields are intentionally restricted
through a controlled whitelist in order to:
- preserve API contract stability
- avoid unsupported property access
- prevent exposing unintended internal fields

The filtering system supports:
- optional combinable filters
- case-insensitive text matching
- partial name search
- inclusive numeric range filtering
- pageable query execution

---

# Future Improvements

- Advanced search capabilities
- Advanced analytics endpoints
- Authentication / authorization
- CI/CD pipeline
- Metrics & monitoring
- Containerized API deployment

---

# Author

Frédérick Beaurienne