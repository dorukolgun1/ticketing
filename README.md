# 🎟️ Ticketing System (Backend)

## Overview
This project is a **real-time event ticketing system backend** built with **Java 21** and **Spring Boot**.  
It provides APIs for **event management, ticket reservations, and purchase workflows** with strong focus on:
- **Data consistency** (idempotency, optimistic/pessimistic locking)
- **Scalability** (Docker, Prometheus, Grafana monitoring)
- **Resilience** (global exception handling, retries, conflict detection)

⚡ **Note:**  
This project does not include a user interface (UI). Instead, all functionality is exposed via REST APIs.  
It is designed as the **backend foundation** for a potential web or mobile app. API endpoints can be tested with **Postman** or integrated with any frontend.

---

## 🔑 Features
- **Event Management**: Create and query events by code.
- **Ticket Inventory**: Maintain stock levels per event & ticket type.
- **Order Management**:
    - Reserve & purchase tickets
    - Idempotency key support (safe retries, no duplicate purchases)
    - Optimistic and pessimistic locking strategies
- **Observability**:
    - Metrics exposed via `/actuator/prometheus`
    - Preconfigured Grafana dashboard for live monitoring
- **Database Migrations**: Flyway for schema versioning.
- **Error Handling**: Global exception handler → returns clear HTTP status codes (`400`, `404`, `409`, `500`).

---

## 🛠️ Tech Stack
- **Java 21** (Virtual Threads enabled for concurrency)
- **Spring Boot 3** (Web, JPA, Validation, Actuator)
- **PostgreSQL** + **Flyway** (DB migrations)
- **Docker Compose** (multi-service orchestration)
- **Micrometer + Prometheus + Grafana** (metrics & dashboards)
- **MapStruct + Lombok** (clean domain & DTO mapping)

---

## 🚀 How to Run

### Prerequisites
- Docker & Docker Compose
- Maven

### Steps
```bash
# 1. Build the project
mvn -DskipTests clean package

# 2. Start all services (app, db, prometheus, grafana, pgadmin)
docker compose up -d --build
Services:

App → http://localhost:8080

Swagger → http://localhost:8080/swagger-ui/index.html

Prometheus → http://localhost:9090

Grafana → http://localhost:3000 (admin / admin123)

PgAdmin → http://localhost:5050

📡 Example API Calls
1. Create Event
http
Copy code
POST http://localhost:8080/api/v1/events
Content-Type: application/json

{
  "code": "EVNT-ROCK-2025",
  "name": "Rock Festival",
  "startTime": "2025-12-01T19:00:00Z",
  "endTime": "2025-12-01T22:00:00Z"
}
2. Purchase Ticket
http
Copy code
POST http://localhost:8080/api/v1/orders/purchase?strategy=optimistic
Content-Type: application/json
Idempotency-Key: 11111111-1111-1111-1111-111111111111

{
  "eventCode": "EVNT-ROCK-2025",
  "ticketType": "STANDARD",
  "quantity": 1
}
3. List Orders
http
Copy code
GET http://localhost:8080/api/v1/orders?event=EVNT-ROCK-2025&page=0&size=10
4. Inventory Check
http
Copy code
GET http://localhost:8080/api/v1/inventory/EVNT-ROCK-2025
📊 Monitoring
Prometheus scrapes application metrics every 5s.

Grafana dashboard includes:

Tickets sold total

Purchase conflicts

Purchase latency (P95, P99)

HTTP request metrics

🎯 Why This Project?
This project demonstrates:

Ability to design scalable backend systems

Applying modern Java features (virtual threads, records, builders)

Building production-like infrastructure (DB migrations, metrics, observability)

Delivering a system that can easily be extended with a frontend in the future.

👨‍💻 Author
Doruk Olgun

Computer Science student @ University of Debrecen

Interested in backend development, cloud, and scalable architectures

yaml
Copy code
