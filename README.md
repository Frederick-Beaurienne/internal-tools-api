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
- [Testing](#testing)
- [Current Progress](#current-progress)
- [Design Choices](#design-choices)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

# Features

- REST API for internal tools management
- Standardized API responses
- PostgreSQL integration
- PostgreSQL native enum mapping
- OpenAPI / Swagger documentation
- Validation & centralized error handling
- Structured application logging
- REST controller testing
- Integration testing
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

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

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
- Validation and error handling tests
- API response contract verification

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
- REST GET and POST endpoints
- REST controller tests
- Integration tests

## In Progress

- PUT and DELETE endpoints
- Analytics endpoints
- DTO mapping refinement

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

---

# Future Improvements

- Complete CRUD operations
- Pagination & filtering
- Advanced analytics endpoints
- Authentication / authorization
- CI/CD pipeline
- Metrics & monitoring
- Containerized API deployment

---

# Author

Frédérick Beaurienne