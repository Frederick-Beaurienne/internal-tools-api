# Internal Tools API

Spring Boot REST API for managing internal SaaS tools with analytics, reporting and cost optimization features.

---

# Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Architecture](#project-architecture)
- [Prerequisites](#prerequisites)
- [Database Setup](#database-setup)
- [Run the Application](#run-the-application)
- [Swagger Documentation](#swagger-documentation)
- [Current Progress](#current-progress)
- [Design Choices](#design-choices)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

# Features

- Tools management CRUD API
- Advanced analytics endpoints
- PostgreSQL integration
- OpenAPI / Swagger documentation
- Validation & centralized error handling
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
├── exception
├── mapper
├── repository
├── service
│   └── impl
└── validation
```

---

# Prerequisites

- Java 17+
- Maven 3.9+
- Docker Desktop
- Git

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

---

# Run the Application

## Start Spring Boot

```bash
mvn spring-boot:run
```

Application available at:

```text
http://localhost:8080
```

---

# Swagger Documentation

Swagger UI:

```text
http://localhost:8080/swagger-ui.html
```

---

# Current Progress

## Completed

- Spring Boot initialization
- PostgreSQL Docker environment
- Database connectivity
- Base project architecture
- Swagger/OpenAPI integration

## In Progress

- JPA entities mapping
- CRUD endpoints
- Validation layer
- Analytics endpoints

---

# Design Choices

## PostgreSQL

Chosen for:
- strong analytical capabilities
- robust aggregation support
- enterprise-grade reliability

## Layered Architecture

The project follows a layered architecture:
- controllers
- services
- repositories
- DTO mapping

This improves:
- maintainability
- separation of concerns
- scalability
- testability

## Hibernate Validation Mode

`ddl-auto=validate` is intentionally used to:
- validate schema consistency
- avoid accidental schema mutations
- preserve provided database structure

---

# Future Improvements

- Integration tests
- Authentication / authorization
- CI/CD pipeline
- Metrics & monitoring
- Containerized API deployment

---

# Author

Frédérick Beaurienne