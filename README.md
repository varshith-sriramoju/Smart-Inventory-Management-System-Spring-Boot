# Smart Inventory Management System

## Running Without Docker

Backend (Windows PowerShell):

```powershell
cd backend
# Set environment variables for this session
$env:DATASOURCE_URL = 'jdbc:mysql://localhost:3306/inventory'
$env:DATASOURCE_USER = 'inventory'
$env:DATASOURCE_PASSWORD = '123456789'

# Start Spring Boot (skip tests for faster startup)
mvnw.cmd -U -DskipTests spring-boot:run
```

Backend (Linux/macOS):

```bash
cd backend
export DATASOURCE_URL='jdbc:mysql://localhost:3306/inventory'
export DATASOURCE_USER='inventory'
export DATASOURCE_PASSWORD='123456789'

# Make wrapper executable once
chmod +x ./mvnw

# Start Spring Boot (skip tests for faster startup)
./mvnw -U -DskipTests spring-boot:run
```

Backend (Google Cloud Shell):

```bash
# Ensure Java 21 is available (Cloud Shell may already have it)
java -version || sudo apt-get update && sudo apt-get install -y openjdk-21-jdk

cd backend
export DATASOURCE_URL='jdbc:mysql://localhost:3306/inventory'
export DATASOURCE_USER='inventory'
export DATASOURCE_PASSWORD='123456789'

./mvnw -U -DskipTests spring-boot:run
# Use Web Preview -> port 5050 to open the app
```

Frontend (Node):

```powershell
cd frontend
npm install

# Optionally point frontend at a non-default API
# set REACT_APP_API_BASE_URL to override http://localhost:5050/api
# PowerShell:
$env:REACT_APP_API_BASE_URL = 'http://localhost:5050/api'

npm start
```

Notes:
- Ensure MySQL is running locally and reachable at `localhost:3306`. Create the `inventory` database and user (`inventory/123456789`) or adjust the env vars above.
- The backend reads env vars first; otherwise it falls back to `jdbc:mysql://db:3306/inventory` (for Docker). For local runs, prefer setting env vars as shown.
- Backend default port is `5050`. Visit `http://localhost:5050/api/health/db` to verify DB connectivity.

Modern Inventory Management System with a Spring Boot (Java 21) backend and a React frontend. The project ships with Docker support (backend, frontend, MySQL) so you can start everything with a single `docker compose up` command.

---

## Highlights

- Full‑stack app: Spring Boot REST API (Java 21, Maven) + React (Create React App)
- JWT authentication and role-based access control
- MySQL persistence (Spring Data JPA / Hibernate)
- File upload support and optional AWS S3 integration
- Dockerized for local development: `docker-compose.yml` included

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.3.5, Java 21, Maven |
| Security | Spring Security, JWT (jjwt) |
| Persistence | Spring Data JPA (Hibernate), MySQL |
| Frontend | React 18 (CRA), Axios, React Router |
| Build & Run | Maven, npm, Docker, Docker Compose |

---

## Repository Layout

```
F:/1/Smart Inventory
├─ backend/                # Spring Boot app (pom.xml, src/)
├─ frontend/               # React app (package.json, src/)
├─ docker-compose.yml      # Compose stack for local development
├─ .dockerignore
└─ README.md
```

---

## Quick Start (Docker)

Requirements: Docker and Docker Compose (v1.27+ or Docker Compose v2).

1. From repository root run:

```powershell
# build images and start services in background
docker compose up -d --build
```

2. Wait for services to become healthy (MySQL initialization may take a few seconds).

3. Open the apps:

- Frontend: http://localhost:3000
- Backend API: http://localhost:5050 (example: `GET /api/products/all`)

4. Stop and remove containers + volumes (data):

```powershell
docker compose down -v
```

Notes:
- Compose creates a MySQL service named `db`. The backend service connects using `jdbc:mysql://db:3306/inventory`.
- The compose file passes environment variables to the backend; these are read by `backend/src/main/resources/application.properties`.

---

## What the Compose stack includes

- `db` — MySQL 8.0 with persistent named volume `db-data`.
- `backend` — built from `backend/Dockerfile`; runs the Spring Boot jar and exposes port `5050`.
- `frontend` — built from `frontend/Dockerfile`; React app built then served by Nginx on port `80` mapped to host `3000`.

If you need production-ready behavior (secrets, backups, scaling), adapt the compose file or use Kubernetes.

---

## Environment and Configuration

You can provide configuration via environment variables (recommended). The backend supports these variables (examples):

```
DATASOURCE_URL     # e.g. jdbc:mysql://db:3306/inventory
DATASOURCE_USER
DATASOURCE_PASSWORD
SERVER_PORT        # default 5050
SECRETE_JWT_STRING # JWT secret
```

Example `.env` (DO NOT commit secrets to VCS):

```
DATASOURCE_URL=jdbc:mysql://db:3306/inventory
DATASOURCE_USER=inventory
DATASOURCE_PASSWORD=123456789
SECRETE_JWT_STRING=change_this_secret
SERVER_PORT=5050
```

Place that file at the repository root if you want docker compose to pick it up, or provide env vars in your CI/CD pipeline.

---

## Local development (without Docker)

Backend (Windows PowerShell):

```powershell
cd backend
# set environment variables for the run (temporary in this session)
$env:DATASOURCE_URL='jdbc:mysql://localhost:3306/inventory'
$env:DATASOURCE_USER='inventory'
$env:DATASOURCE_PASSWORD='123456789'
mvnw.cmd -U spring-boot:run
```

Frontend (dev server):

```powershell
cd frontend
npm install
npm start
```

The frontend dev server expects the backend at `http://localhost:5050`; adjust `frontend/src/service/ApiService.js` if needed.

---

## Healthchecks & Startup ordering

The Compose stack provided is simple and relies on Docker's `depends_on` so the backend starts after the DB container creation. For robustness in CI/dev you should add a startup wait strategy (e.g., `wait-for-it.sh`, `dockerize`, or a Spring retry on startup) so the backend retries connecting until MySQL is ready.

If you want, I can add a small `wait-for` entrypoint to the backend container so it waits until port 3306 on `db` is reachable before starting the JVM.

---

## Running tests

Backend unit/integration tests (Maven):

```powershell
cd backend
mvn test
```

Frontend tests:

```powershell
cd frontend
npm test
```

---

## Troubleshooting

- "Driver ... claims to not accept jdbcUrl, ${DATASOURCE_URL}" — means Spring didn't receive the env var. Fix by exporting env vars (see Local development) or running via Compose which passes them.
- DB connection refused — ensure Docker engine running, or that MySQL is listening on `localhost:3306` for local runs.
- Port conflicts — change ports in `docker-compose.yml` or `application.properties`.

---

## Production notes

- For production builds, create images using a secure build pipeline, store secrets in a secrets manager, and use a managed database or apply secure backups.
- Consider using a multi-stage build with a non-root runtime user for the backend image (the current `backend/Dockerfile` is multi-stage).

---

## Upgrade notes (Spring Boot 3.5.x)

When upgrading:

1. Update the parent version in `backend/pom.xml` to `3.5.x`.
2. Run the full test suite and manually check security config (Spring Security may introduce configuration changes across minor versions).
3. Read the Spring Boot 3.5 release notes for property name changes.

---

## Contributing

1. Create a branch for your changes.
2. Run unit tests: `mvn test` (backend) and `npm test` (frontend).
3. Open a pull request describing the change.

---

If you'd like, I can also:

- Add a `wait-for` wrapper so the backend waits for MySQL readiness before starting.
- Add Compose healthchecks and a small `init.sql` to seed the database with test data.
- Add GitHub Actions workflows to build Docker images and run tests in CI.

Tell me which of the above you'd like me to implement next.
