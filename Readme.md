# SalesDeepDive ERP – Backend (Spring Boot)

A Spring Boot REST API backend for the SalesDeepDive ERP system.  
Built with Maven, packaged as a JAR, and shipped inside a Docker container.

---

## Table of Contents

1. [Prerequisites](#1-prerequisites)
2. [Environment variables](#2-environment-variables)
3. [Run locally (without Docker)](#3-run-locally-without-docker)
4. [Deploy – Local Docker (dev)](#4-deploy--local-docker-dev)
5. [Deploy – Staging / Production](#5-deploy--staging--production)
6. [Useful commands](#6-useful-commands)

---

## 1. Prerequisites

| Tool | Version |
|---|---|
| Java (JDK) | 11 |
| Maven | 3.9+ (or use the included `./mvnw` wrapper) |
| Docker & Docker Compose | latest |

---

## 2. Environment variables

| Variable | Default | Purpose |
|---|---|---|
| `SPRING_PROFILES_ACTIVE` | `docker` | Spring profile (`docker` / `staging`) |
| `ERP_DB_URL` | *(required)* | JDBC URL e.g. `jdbc:mysql://host:3306/sales_deep_dive_erp` |
| `ERP_DB_USERNAME` | *(required)* | Database username |
| `ERP_DB_PASSWORD` | *(required)* | Database password |
| `ERP_APP_NAME` | `production-deep-dive-backend` | Internal app identifier |
| `ERP_JWT_SECRET` | *(required in staging)* | JWT signing secret |
| `ERP_BRANDING_APP_NAME` | `Sales Deep Dive` | App name used in emails / responses |
| `ERP_BRANDING_COMPANY_NAME` | `Ecoshelter` | Company name used in emails / responses |
| `ERP_FRONTEND_URL` | `http://localhost:4300/` | Frontend origin (used in CORS & email links) |
| `MAIL_HOST` | `smtp.gmail.com` | SMTP host |
| `MAIL_PORT` | `465` | SMTP port |
| `MAIL_USERNAME` | *(required)* | SMTP username |
| `MAIL_PASSWORD` | *(required)* | SMTP password |
| `MAIL_FROM_ADDRESS` | *(required)* | From address |
| `SYNC_ERROR_EMAIL_TO` | `timothymoswa@gmail.com` | Recipient for sync error alerts |

---

## 3. Run locally (without Docker)

```bash
cd SalesDeepDiveProductionERP_Backend

# Using Maven wrapper
./mvnw spring-boot:run \
  -Dspring-boot.run.profiles=docker \
  -Dspring-boot.run.jvmArguments="-DERP_DB_URL=jdbc:mysql://localhost:3306/sales_deep_dive_erp -DERP_DB_USERNAME=root -DERP_DB_PASSWORD=yourpassword"
```

The API will be available at `http://localhost:8087`.

---

## 4. Deploy – Local Docker (dev)

> **Context:** Run from the **project root** (`0_SalesDeepDiveSystem-Backend/`).  
> Uses `docker-compose.erp.yml`. Backend is accessible at `http://localhost:8087`.

### Build & start (all services)

```bash
docker compose -f docker-compose.erp.yml up -d --build
```

### Rebuild **only** the backend after code changes

```bash
docker compose -f docker-compose.erp.yml build --no-cache erp-backend
docker compose -f docker-compose.erp.yml up -d erp-backend
```

### View logs

```bash
docker compose -f docker-compose.erp.yml logs -f erp-backend
```

### Stop all services

```bash
docker compose -f docker-compose.erp.yml down
```

### Health check

```bash
curl http://localhost:8087/actuator/health
```

---

## 5. Deploy – Staging / Production

> **Server:** `root@95.111.230.230`  
> **Deploy dir:** `/var/www/erp-staging`  
> **Live URL:** `https://production-backend.ecoshelter.co.zw`  
> Uses `docker-compose.staging.yml` + `.env.staging`.

### 5a. First-time / full deployment

From the project root on your local machine:

```bash
chmod +x deploy-staging.sh
./deploy-staging.sh
```

The script will:
1. Install Docker on the server (if absent)
2. Create the MySQL database and user
3. Sync project files via `rsync`
4. Build Docker images and start containers
5. Configure Apache Virtual Hosts
6. Obtain Let's Encrypt SSL certificates

### 5b. Redeploy backend only (after making code changes)

**Step 1 – Push changed source files to the server:**

```bash
cd /home/tmoswa/Projects/Docker/laravel-apps/0_SalesDeepDiveSystem-Backend

rsync -azP \
  --exclude='.git/' \
  --exclude='target/' \
  ./SalesDeepDiveProductionERP_Backend/ \
  root@95.111.230.230:/var/www/erp-staging/SalesDeepDiveProductionERP_Backend/
```

**Step 2 – Rebuild and restart the backend container on the server:**

```bash
ssh root@95.111.230.230 '
  cd /var/www/erp-staging &&
  docker compose -f docker-compose.staging.yml --env-file .env build --no-cache erp-backend &&
  docker compose -f docker-compose.staging.yml --env-file .env up -d erp-backend &&
  docker compose -f docker-compose.staging.yml ps
'
```

> The Docker build runs `mvn -DskipTests package` inside the container, so Maven does **not** need to be installed on the server.

### 5c. Environment / config-only change (no code change)

If you only changed variables in `.env.staging` (e.g. JWT secret, mail credentials):

```bash
# Copy updated env file
scp .env.staging root@95.111.230.230:/var/www/erp-staging/.env

# Restart the backend container
ssh root@95.111.230.230 '
  cd /var/www/erp-staging &&
  docker compose -f docker-compose.staging.yml --env-file .env up -d erp-backend
'
```

### 5d. Verify the deployment

```bash
ssh root@95.111.230.230 '
  docker compose -f /var/www/erp-staging/docker-compose.staging.yml ps
  curl -sf http://127.0.0.1:8087/actuator/health
'
```

---

## 6. Useful commands

| Task | Command (run on server or prefix with `ssh root@95.111.230.230`) |
|---|---|
| Tail backend logs | `docker compose -f /var/www/erp-staging/docker-compose.staging.yml logs -f erp-backend` |
| Restart backend | `docker compose -f /var/www/erp-staging/docker-compose.staging.yml restart erp-backend` |
| Shell into container | `docker exec -it erp-backend-staging sh` |
| Health check | `curl http://127.0.0.1:8087/actuator/health` |
| List all services | `docker compose -f /var/www/erp-staging/docker-compose.staging.yml ps` |
| View all container logs | `docker compose -f /var/www/erp-staging/docker-compose.staging.yml logs -f` |
