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
- [Insomnia Collection](#insomnia-collection)
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
- Real usage metrics aggregation
- Department cost analytics
- Department-level cost distribution insights
- Company budget aggregation
- Defensive analytics sorting
- Empty analytics dataset handling
- Expensive tools analytics
- Company-wide cost efficiency benchmark
- Cost optimization insights
- Database-driven analytics filtering
- Efficiency rating analytics
- Category analytics
- Budget distribution analytics
- Category efficiency insights
- Insomnia API collection export

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

## Architecture Overview

The project follows a layered and partially feature-oriented architecture.

### Core Modules

- `tools`
  Contains the main business domain:
  entities, DTOs, repositories, services and mappers related to tool management.

- `analytics`
  Contains reporting and metrics-oriented business logic.
  This module prepares future analytical and optimization features.

### Shared Technical Layers

- `controller`
  Exposes the REST API endpoints.

- `exception`
  Centralizes structured API exception handling.

- `validation`
  Contains custom validation logic and constraints.

### Main Architectural Principles

- Separation of concerns
- DTO-based API contracts
- Dedicated mapper layer
- Repository pattern with Spring Data JPA
- Pagination and filtering abstraction
- Analytics-ready modular structure

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

# Insomnia Collection

An Insomnia export collection is provided
to simplify API exploration and testing.

Location:

```text
docs/insomnia/
```

The collection includes:

- CRUD endpoints
- filtering examples
- analytics endpoints
- validation scenarios
- documented test requests

Import into Insomnia using:

```text
Application → Import → From File
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
- Validation and exception handling tests
- CRUD endpoint workflow testing
- PostgreSQL enum integration testing
- API response contract verification
- Dynamic filtering endpoint testing
- Query parameter binding verification
- Analytics endpoint testing
- Analytics response contract verification
- Analytics sorting validation
- Analytics error handling validation
- Empty analytics scenario verification
- Analytics filtering and limit validation
- Analytics optimization workflow testing
- Structured analytics validation errors
- Expensive tools endpoint verification
- Category analytics endpoint verification
- Analytics aggregation workflow validation

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
- Real usage analytics aggregation
- Category analytics endpoint
- Cost optimization analytics
- Database-driven reporting aggregation

## In Progress

- Analytics and reporting expansion
- Additional optimization endpoints

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

## Analytics Architecture

Analytics responsibilities are intentionally isolated
from the main business domain.

The `analytics` module acts as a read-oriented layer
responsible for reporting and usage aggregation logic,
while the `tools` module remains the primary business domain.

This separation was chosen to:
- preserve separation of concerns
- avoid polluting core CRUD business logic
- prepare future analytical features
- keep reporting logic modular and extensible

## Database-Driven Analytics Strategy

Analytics queries intentionally prioritize
database-level computation whenever possible.

This approach was chosen to:

- reduce unnecessary data transfer
  between database and backend
- avoid loading large datasets
  only to aggregate them in Java
- leverage PostgreSQL aggregation
  and sorting capabilities
- preserve scalability for future
  analytics growth

Business aggregation, filtering and
sorting are therefore primarily pushed
to the database layer, while Java
focuses on:

- orchestration
- response composition
- controlled rounding
- defensive contract preservation

## Department Cost Analytics Strategy

The `/api/analytics/department-costs`
endpoint follows a business-oriented
analytics strategy.

Analytics behavior intentionally differs
from CRUD endpoints in order to provide
stable reporting contracts.

Implemented rules:

- only active tools are included
- financial values use controlled rounding
  through `NumericService`
- department aggregation is performed
  at database level
- sorting is primarily executed through
  Criteria API queries
- defensive Java post-sorting preserves
  sorting contracts after analytics
  dataset enrichment
- departments without active tools are
  returned with zero-valued metrics
- empty analytics datasets return
  explicit business-oriented responses

Empty analytics responses intentionally use:

```json
{
  "data": [],
  "message": "No analytics data available - ensure tools data exists",
  "summary": {
    "total_company_cost": 0
  }
}
```
This approach was chosen to:
- preserve API compatibility
- maintain stable response contracts
- expose predictable analytical behavior
- support future reporting expansion

## Expensive Tools Analytics Strategy

The `/api/analytics/expensive-tools`
endpoint follows an optimization-oriented
analytics strategy.

The endpoint is designed to surface
potentially expensive or inefficient tools
while preserving stable analytical
contracts.

Implemented rules:

- only active tools are included
- filtering and result limiting are executed
  at database level
- expensive tools are ordered by
  monthly cost descending
- cost-per-user calculations use
  controlled rounding through
  `NumericService`
- company benchmark uses a weighted
  enterprise-wide calculation:

```text
SUM(monthly_cost)
/
SUM(active_users_count)
```

- tools with zero users are excluded
  from benchmark calculation
- tools with zero users receive
  `not_applicable`
  efficiency rating
- empty analytics datasets return
  explicit business-oriented responses

This approach was chosen to:

- preserve analytical consistency
- avoid benchmark distortion
- reduce unnecessary database/backend
  data transfer
- leverage PostgreSQL filtering
  and sorting capabilities
- support future optimization
  and reporting features

## Incremental Analytics Delivery Strategy

Given the exercise timeframe and progressive
delivery constraints, a deliberate design
choice was made regarding analytics features.

The project prioritizes delivering fully
functional and production-oriented analytics
features, even if limited in scope, rather
than partially implemented or incomplete
reporting capabilities.

This strategy was chosen to:

- preserve functional coherence
- maintain stable API contracts
- avoid unfinished business logic
- ensure meaningful testing coverage
- favor demonstrable software quality
  over feature quantity

Implemented analytics endpoints therefore
represent complete and operational
business workflows rather than prototypes
or partially implemented features.

---

# Future Improvements

- Advanced search capabilities
- Underutilized tools analytics endpoint
- Vendor summary analytics endpoint
- Advanced analytics expansion
- Authentication / authorization
- CI/CD pipeline
- Metrics & monitoring
- Containerized API deployment

---

# Author

Frédérick Beaurienne