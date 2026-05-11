# Content Distribution API

A Spring Boot REST API for managing film data with advanced querying capabilities, including dynamic filtering, pagination, and sorting.

The application demonstrates production-style backend patterns such as layered architecture, DTO-based validation, centralized exception handling, and database schema versioning with Flyway.

## Features

- CRUD operations for films
- Partial updates (PATCH)
- Pagination and sorting
- Dynamic filtering (title contains, genre exact match, release year exact match)
- Global exception handling
- Input validation
- PostgreSQL integration
- Flyway database migrations

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Data JPA
- PostgreSQL
- Flyway
- Maven

## Setup

1. Clone the repo
2. Create a PostgreSQL database:
   CREATE DATABASE content_distribution_api;

3. Copy:
   application-dev.example.properties → application-dev.properties

4. Fill in your DB credentials

5. Run the application:
   mvn spring-boot:run
   
## API Examples

### Create Film
```http
POST /films
```
```json
{
  "title": "Alien",
  "genre": "Sci-Fi Horror",
  "releaseYear": 1979
}
```
### Get Films
```http
GET /films?page=0&size=5&sortBy=title
```
### Filter
```http
GET /films?title=alien
GET /films?genre=Sci-Fi Horror
GET /films?releaseYear=1979
```
### Example Response
```http
GET /films?title=alien
```
```json
{
  "data": [
    {
      "id": 1,
      "title": "Alien",
      "genre": "Sci-Fi Horror",
      "releaseYear": 1979
    }
  ],
  "page": 0,
  "size": 10,
  "totalElements": 1,
  "totalPages": 1
}
```
## Notes

- Database schema is managed via Flyway migrations
- Duplicate films (same title + release year) are prevented

## Key Design Decisions

- **DTO Layer**: Separates API contract from persistence model for flexibility and validation control.
- **Specification Pattern**: Enables dynamic query composition for filtering without hard-coded SQL.
- **Custom Pagination Wrapper**: Avoids exposing Spring's internal `Page` structure to clients.
- **Centralized Exception Handling**: Ensures consistent API error responses.
- **Flyway Migrations**: Provides version-controlled and reproducible database schema management.

## Future Enhancements

- Watchlist/queue system for structured film consumption
- Film ratings and user notes
- Recommendation logic based on viewing patterns
