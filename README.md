# 🎟️ Real-Time Event Ticketing System (Backend)

![CI Pipeline](https://github.com/dorukolgun1/ticketing/actions/workflows/ci.yml/badge.svg)
![Kubernetes Tested](https://img.shields.io/badge/Kubernetes-Tested-brightgreen?logo=kubernetes&logoColor=white)

---

## 🚀 Project Vision

Imagine thousands of fans trying to buy concert tickets at the same time.  
This system was designed to handle exactly that scenario.

The **Ticketing System** is a **high-concurrency backend** built with **Java 21** and **Spring Boot 3**, focusing on:
- ⚡ **Speed & Concurrency** → Virtual Threads for handling massive parallel requests
- 🔒 **Data Integrity** → Optimistic & Pessimistic Locking to prevent overselling
- 📊 **Observability** → Live metrics and dashboards with Prometheus & Grafana
- 🛡️ **Resilience** → Idempotency keys, retry mechanisms, and robust error handling

This is not just a demo – it’s a **production-style backend foundation** that could power a real-world ticketing platform.

---

## ✨ Key Highlights
- **Idempotent Purchases** → no double-charging with same idempotency key
- **Locking Strategies** → optimistic & pessimistic locking supported
- **Scalable Observability** → Prometheus + Grafana dashboards for latency, conflicts, throughput
- **Resilient Architecture**
  - Automatic DB migrations with Flyway
  - Global Exception Handler → clean error responses (`400`, `404`, `409`, `500`)
  - Retry on concurrency conflicts
- **API-First Approach** → documented REST API (Swagger), ready for frontend/mobile integration

---

## 🛠️ Technology Stack
- **Language**: Java 21 (Virtual Threads, Records, Builders)
- **Framework**: Spring Boot 3 (Web, Data JPA, Validation, Actuator)
- **Database**: PostgreSQL 16 + Flyway migrations
- **Containerization**: Docker & Docker Compose
- **Monitoring**: Micrometer + Prometheus + Grafana
- **Boilerplate Reduction**: MapStruct & Lombok

---

## 🏗️ How It Works
1. **Events** are created with codes (e.g., `EVNT-ROCK-2025`).
2. **Inventory** is initialized per ticket type (VIP, Standard, Student).
3. **Orders** are placed:
  - User sends a `PurchaseRequest` with `Idempotency-Key`.
  - System reserves stock → saves order → returns confirmation.
4. **Concurrency Safety**:
  - If 1000 users hit “Buy” simultaneously, some succeed instantly, others retry/wait.

---

## 🖥️ Running with Docker

### Prerequisites
- Docker + Docker Compose
- Maven

### Setup
```bash
# Build
mvn -DskipTests clean package

# Run everything (app, db, pgadmin, prometheus, grafana)
docker compose up -d --build
Services
API → http://localhost:8080

Swagger → http://localhost:8080/swagger-ui/index.html

Prometheus → http://localhost:9090

Grafana → http://localhost:3000

PgAdmin → http://localhost:5050

☸️ Running on Kubernetes
Tested locally with Minikube.

Start Minikube
bash
Copy code
minikube start --cpus=4 --memory=6g --driver=docker
Build & Load Docker Image
bash
Copy code
docker build -t ticketing-app:v1 .
minikube image load ticketing-app:v1 --overwrite=true
Apply Manifests
bash
Copy code
kubectl apply -f k8s/
Access Service
Port-forward quick test:

bash
Copy code
kubectl -n ticketing port-forward svc/ticketing-app 8080:8080
→ http://localhost:8080/actuator/health

Or via NodePort:

bash
Copy code
kubectl -n ticketing patch svc/ticketing-app -p '{"spec":{"type":"NodePort"}}'
minikube service ticketing-app -n ticketing --url
Scaling
bash
Copy code
kubectl -n ticketing scale deploy/ticketing-app --replicas=3
📡 API Examples
Create Event
http
Copy code
POST /api/v1/events
{
  "code": "EVNT-ROCK-2025",
  "name": "Rock Festival",
  "startTime": "2025-12-01T19:00:00Z",
  "endTime": "2025-12-01T22:00:00Z"
}
Purchase Ticket
http
Copy code
POST /api/v1/orders/purchase?strategy=optimistic
Idempotency-Key: 123e4567-e89b-12d3-a456-426614174000
{
  "eventCode": "EVNT-ROCK-2025",
  "ticketType": "STANDARD",
  "quantity": 2
}
List Orders
http
Copy code
GET /api/v1/orders?event=EVNT-ROCK-2025&page=0&size=10
Check Inventory
http
Copy code
GET /api/v1/inventory/EVNT-ROCK-2025
📊 Monitoring & Metrics
Prometheus scrapes /actuator/prometheus every 5s.

Grafana dashboard includes:

🎫 Total Tickets Sold

⚠️ Purchase Conflicts

⏱️ Latency (P95/P99)

🌐 HTTP Request Rates (Throughput 🚀)

This makes bottlenecks and conflicts visible in real time.

🎯 Why This Project Stands Out
Demonstrates real-world backend challenges: concurrency, consistency, observability

Uses modern Java (virtual threads) for high-concurrency performance

Structured for scalability → frontend can be easily added

Shows both engineering depth and production readiness

👨‍💻 Author
Doruk Olgun
🎓 Computer Science Student @ University of Debrecen
💡 Passionate about backend development, cloud computing, and scalable systems
🌍 Loves building systems that solve real-world concurrency problems