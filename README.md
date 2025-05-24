# 📬 SparkMail: Email Microservice for SparkFund

**SparkMail** is a dedicated RESTful microservice built for the [SparkFund](https://github.com/KalinKarolev/SparkFund-App) crowdfunding platform. It handles transactional email notifications triggered by major events within SparkFund, such as donations, campaign completions or cancellations, and admin responses to user signals.

This Spring Boot-based service follows clean architecture principles and is designed to be lightweight, modular, and production-ready for integration with larger platforms.

---

## 📖 About

SparkMail enables SparkFund to send email notifications to users for:

- 💸 New donations to their Sparks (fundraising campaigns)
- ✅ Completion of a Spark
- ❌ Cancellation of a Spark
- 🛠️ Resolution of user signals by administrators

Emails are sent through SMTP and stored in a relational database for history tracking and potential auditing.

---

## ✨ Features

- **RESTful Email Sending API** – Trigger emails via HTTP POST requests.
- **Email Persistence** – Save sent emails with metadata including recipient, subject, body, and timestamp.
- **Spring Mail Integration** – Uses Spring Boot's `JavaMailSender` for SMTP delivery.
- **OpenAPI Documentation** – Interactive Swagger UI included.
- **Input Validation** – Enforces valid email payloads using annotations and exception handling.

---

## 🚀 Tech Stack

### 🔧 Backend
- **Java 17**
- **Spring Boot 3.4.0**

### 🧩 Spring Boot Starters
- `spring-boot-starter-web` – REST API support
- `spring-boot-starter-data-jpa` – Database interaction via Hibernate
- `spring-boot-starter-mail` – Email delivery
- `spring-boot-starter-validation` – Input validation
- `spring-boot-starter-test` – Test utilities

### 📦 Other Libraries
- **Lombok** – For boilerplate reduction
- **SpringDoc OpenAPI** – Swagger UI generation

### 🗃️ Database
- **MySQL** – Production database
- **H2** – In-memory dev/test database

### 🛠 Build & Tools
- **Maven** – Dependency and build management
- `spring-boot-maven-plugin` – Package and run easily

---

#### Sample Request Body
```json
{
  "to": "recipient@example.com",
  "subject": "Donation Received!",
  "body": "Thank you for supporting the Spark."
}
