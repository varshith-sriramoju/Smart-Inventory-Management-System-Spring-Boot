# 📦 Smart Inventory Management System

A modern, full-stack **Inventory Management System** built using
**Spring Boot (Java 21)** + **React**, with **MySQL**, **JWT Authentication**, and full **Docker** support.

This system includes product & category management, image uploads, authentication, analytics — everything needed for a scalable inventory workflow.

---

## 🚀 Features

- 🔐 JWT Authentication + Role-based Access
- 🗄️ MySQL Database (Spring Data JPA / Hibernate)
- 📡 REST API with Spring Boot 3
- 🎨 React Frontend (CRA)
- 🐳 Full Docker Compose Setup
- ⚙️ Env-based configuration (local + cloud ready)
- 🩺 Built-in healthcheck endpoints

---

## 🗂️ Project Structure

```
Smart-Inventory-Management-System/
├── backend/            # Spring Boot Application
├── frontend/           # React Application
├── docker-compose.yml  # Stack: Backend + Frontend + MySQL
└── README.md
```

---

# ⚙️ Backend – Run Without Docker

## **Windows PowerShell**

```powershell
cd backend

# Environment variables for this session
$env:DATASOURCE_URL = 'jdbc:mysql://localhost:3306/inventory'
$env:DATASOURCE_USER = 'inventory'
$env:DATASOURCE_PASSWORD = '123456789'

# Start Spring Boot
.\mvnw.cmd -U -DskipTests spring-boot:run
```

---

## **Linux / macOS**

```bash
cd backend

export DATASOURCE_URL='jdbc:mysql://localhost:3306/inventory'
export DATASOURCE_USER='inventory'
export DATASOURCE_PASSWORD='123456789'

# Make wrapper executable (first time only)
chmod +x mvnw

./mvnw -U -DskipTests spring-boot:run
```

---

## **Google Cloud Shell**

```bash
# Install Java 21 if needed
java -version || sudo apt-get update && sudo apt-get install -y openjdk-21-jdk

cd backend

export DATASOURCE_URL='jdbc:mysql://localhost:3306/inventory'
export DATASOURCE_USER='inventory'
export DATASOURCE_PASSWORD='123456789'

./mvnw -U -DskipTests spring-boot:run

# Use Web Preview → Port 5050
```

Backend URL:
```
http://localhost:5050
```

---

# 🎨 Frontend – Run Without Docker

```powershell
cd frontend
npm install

# Optional custom API base URL
$env:REACT_APP_API_BASE_URL = 'http://localhost:5050/api'

npm start
```

Frontend dev server:
```
http://localhost:3000
```

---

# 🛢️ MySQL Setup (Local Install)

```sql
CREATE DATABASE IF NOT EXISTS inventory CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'inventory'@'localhost' IDENTIFIED BY '123456789';

GRANT ALL PRIVILEGES ON inventory.* TO 'inventory'@'localhost';

FLUSH PRIVILEGES;
```

Environment variables:

```
DATASOURCE_URL=jdbc:mysql://localhost:3306/inventory
DATASOURCE_USER=inventory
DATASOURCE_PASSWORD=123456789
```

Spring Boot fallback configuration:

```
spring.datasource.url=${DATASOURCE_URL:jdbc:mysql://db:3306/inventory}
spring.datasource.username=${DATASOURCE_USER:inventory}
spring.datasource.password=${DATASOURCE_PASSWORD:123456789}
```

---

# 🐳 Run Entire Stack With Docker

Requires Docker Engine or Docker Desktop.

## Start full stack:

```bash
docker compose up -d --build
```

Services:

| Service   | URL |
|-----------|-----|
| Frontend  | http://localhost:3000 |
| Backend   | http://localhost:5050 |
| MySQL     | localhost:3306 |

Stop & clean:

```bash
docker compose down -v
```

---

# 🔧 Environment Variables (.env example)

```
DATASOURCE_URL=jdbc:mysql://db:3306/inventory
DATASOURCE_USER=inventory
DATASOURCE_PASSWORD=123456789
SECRETE_JWT_STRING=change_this_secret
SERVER_PORT=5050
```

> **Never commit real secrets to Git.**

---

# 🧪 Running Tests

## Backend
```bash
cd backend
mvn test
```

## Frontend
```bash
cd frontend
npm test
```

---

# ❤️ Troubleshooting

| Issue | Cause | Fix |
|------|--------|------|
| Cannot connect to DB | MySQL not running or wrong credentials | Start MySQL / update env vars |
| UnknownHost `db` | Running without Docker | Use `localhost` for local runs |
| `${DATASOURCE_URL}` appears literally | Env vars missing | Export variables before running |
| Port conflict | Port already in use | Change port or stop other service |

Healthcheck endpoints:
```
/api/health/db
/api/health/app
```

---

# 🌐 Production Notes

- Use a real secrets manager (AWS, GCP, Docker Secrets)
- Deploy backend with JAR or Docker image
- Use managed database services (RDS, Cloud SQL)
- Harden Docker images (non-root user, multi-stage builds)

---


