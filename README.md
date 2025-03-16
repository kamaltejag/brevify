# Brevify URL Shortener

Brevify (Derived from "brevity," emphasizing concise links) is a URL shortener application built using Spring Boot. It
provides a simple and efficient way to shorten URLs,
manage short codes, and redirect users to the original long URLs. The application leverages caching, Kafka messaging,
and PostgreSQL for data persistence.

---

## Setup Guide

### Prerequisites

- Java 21
- Maven
- PostgreSQL
- Docker (for running the database)

### Installation Steps

1. **Clone the repository:**

   ```bash
   git clone https://github.com/kamaltejag/brevify.git
   cd brevify
   ```

2. **Download and install the docker images:**
   ```bash
   docker-compose up -d   
   ```
3. **Configure the database:**

   Ensure PostgreSQL is running and create a database named `brevify`. Update the database connection details
   in `application.properties`.

3. **Build the project:**

   ```bash
   mvn clean install
   ```

4. **Run the application:**

   ```bash
   mvn spring-boot:run
   ```

---

## Usage Instructions

### Shorten a URL

Send a POST request to `/shorten` with a JSON body containing the URL to be shortened.

### Retrieve Short Code Data

Send a GET request to `/code/{shortCode}` to get details about the short code.

### Redirect to Long URL

Access `/{shortCode}` to be redirected to the original long URL.

---

## Architecture Overview

### Components

- **Controller:** Handles HTTP requests and responses.
- **Service:** Contains business logic for URL shortening and retrieval.
- **Repository:** Manages database interactions.

### Data Flow

1. User sends a request to shorten a URL.
2. The controller processes the request and calls the service.
3. The service generates a short code and stores it in the database.
4. The user receives the short code.

---

## High-Level Design (HLD)

### Overview

The Brevify application is designed to handle URL shortening efficiently with a focus on scalability and performance. It
integrates with Kafka for message processing and Redis for caching.

### Components

- **Spring Boot Application:** Core application logic and REST API.
- **PostgreSQL:** Persistent storage for URL mappings.
- **Kafka:** Message broker for processing URL shortening requests asynchronously.
- **Redis:** In-memory data store for caching frequently accessed URLs.

### Data Flow

1. **URL Shortening:**
    - User sends a request to shorten a URL.
    - The request is processed and a short code is generated.
    - The short code and original URL are stored in PostgreSQL.
    - A message is sent to Kafka for further processing.

2. **URL Retrieval:**
    - User requests the original URL using a short code.
    - The application checks Redis for a cached entry.
    - If not found, it retrieves the URL from PostgreSQL and caches it in Redis.

3. **Kafka Integration:**
    - Kafka is used to handle high-throughput URL shortening requests.
    - Messages are produced and consumed for processing URL data.

4. **Redis Caching:**
    - Redis is used to cache URL mappings for quick access.
    - Reduces load on the database and improves response times.

---

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request. Ensure your code follows the project's
coding standards.

---

## Contact

For questions or support, please contact: [kamaltejag@gmail.com](kamaltejag@gmail.com).


---

## [License](license.md)

This project is licensed under the MIT License. See the [LICENSE.md](../LICENSE.md) file for details.