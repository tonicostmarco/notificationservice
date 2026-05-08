# 📨 notification-service

A Spring Boot service that processes payment notifications asynchronously using RabbitMQ. Built for learning purposes, focused on understanding message-driven architecture and async communication between services.

![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-brightgreen?style=flat-square&logo=springboot)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Topic_Exchange-FF6600?style=flat-square&logo=rabbitmq)
![MongoDB](https://img.shields.io/badge/MongoDB-7-47A248?style=flat-square&logo=mongodb)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker)

---

## 🧭 Why async messaging?

In a synchronous system, if the notification service is down, the entire payment flow fails — even if the payment itself was processed successfully. That's unacceptable.

With RabbitMQ, the payment service publishes a message and moves on. The notification service consumes that message whenever it's available. A consumer failure does not affect the producer. The systems are decoupled.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.0.6 |
| Messaging | Spring AMQP · RabbitMQ (Topic Exchange) |
| Persistence | Spring Data MongoDB |
| Validation | Bean Validation (Jakarta) |
| Infrastructure | Docker / Docker Compose |

---

## 🏛️ Architecture

```
POST /payments/notify
        │
        ▼
  PaymentController
  deserializes JSON → PaymentDTO
        │
        ▼
  RabbitTemplate.convertAndSend()
  routing key = "payment." + status.toLowerCase()
        │
        ▼
  TopicExchange
  routes to matching queue
        │
        ▼
  PaymentConsumer (@RabbitListener)
  idempotency check → existsByTransactionId()
        │
        ├── duplicate → discard
        └── new → save to MongoDB
```

### RabbitMQ setup

| Queue | Routing key |
|---|---|
| `payment.paid` | `payment.paid` |
| `payment.failed` | `payment.failed` |
| `payment.pending` | `payment.pending` |

### Idempotency

Before persisting, the consumer checks whether the `transactionId` already exists in MongoDB. Duplicate messages are silently discarded. This prevents double-processing in case of redelivery.

---

## 🔌 Endpoints

### Notify payment

```http
POST /payments/notify
```

```json
{
  "transactionId": "txn-001",
  "amount": 150.0,
  "status": "PAID",
  "customerEmail": "user@example.com"
}
```

Valid values for `status`: `PAID` · `FAILED` · `PENDING`

**Response:** `200 OK` with the received payload.

---

## 🛠️ Running Locally

### Prerequisites

| Tool | Minimum Version |
|---|---|
| Java | 25 |
| Maven | 3.9.x |
| Docker + Docker Compose | any recent version |

```bash
# 1. Clone the repository
git clone https://github.com/tonicostmarco/notification-service.git
cd notification-service

# 2. Start RabbitMQ and MongoDB
docker compose up -d

# 3. Verify containers are running
docker ps

# 4. Start the application
./mvnw spring-boot:run
```

**RabbitMQ management UI:** [http://localhost:15672](http://localhost:15672) — `guest` / `guest`

---

## 🎯 Technical Decisions

| Decision | Reason |
|---|---|
| Topic Exchange over Direct Exchange | Both work here, but Topic allows pattern-based routing (`payment.*`), making it easier to add new status types without reconfiguring the exchange |
| Idempotency at consumer level | RabbitMQ can redeliver messages on consumer failure. Without idempotency, the same transaction could be saved multiple times. The check runs before every save |
| `JacksonJsonMessageConverter` | Replaces default Java binary serialization. JSON makes messages readable in the RabbitMQ management UI and interoperable with other services |
| Separate consumer per queue | Each method has a single responsibility. A single consumer with `if/switch` on status would mix concerns |

---

## ⚠️ Known Limitations

This project was built strictly to understand async messaging patterns. It is intentionally minimal:

- No email sender — notifications are not actually delivered
- No authentication
- No CRUD endpoints
- No business rules beyond idempotency
- No deployment
- MongoDB runs without authentication in local dev
