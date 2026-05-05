# ESG Benchmark Comparator

## 🌍 Overview
The **ESG Benchmark Comparator** is a robust Spring Boot 3 application designed to store, manage, and compare Environmental, Social, and Governance (ESG) records for various companies. It provides a secure RESTful API with integrated JWT authentication, Redis caching for high performance, and a containerized environment for easy deployment.

## 🛠 Tech Stack
- **Language:** Java 17
- **Framework:** Spring Boot 3.2.5
- **Security:** Spring Security, JWT (JJWT)
- **Database:** PostgreSQL 15
- **Caching:** Redis 7
- **Persistence:** Spring Data JPA / Hibernate
- **Build Tool:** Maven
- **Containerization:** Docker & Docker Compose
- **Utilities:** Lombok, Jakarta Validation

## 📁 Folder Structure
```text
ESG-Benchmark-Comparator/
├── backend/
│   ├── src/main/java/com/internship/tool/
│   │   ├── config/          # Configuration (Security, Redis, JPA)
│   │   ├── controller/      # REST Controllers (Auth, ESG, Health)
│   │   ├── dto/             # Data Transfer Objects
│   │   ├── entity/          # JPA Entities (User, ESG Records)
│   │   ├── exception/       # Custom Exceptions & Global Handler
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   ├── security/        # JWT & Auth logic (Util, Filter, Service)
│   │   └── service/         # Business Logic Layer
│   ├── src/main/resources/
│   │   └── application.yml  # Application Configuration
│   ├── Dockerfile           # Multi-stage Docker build
│   └── pom.xml              # Maven dependencies
├── docker-compose.yml       # Infrastructure orchestration
├── .env.example             # Environment variables template
├── SECURITY.md              # Security policy
└── README.md                # Project documentation
```

## 🚀 Setup & Installation

### Prerequisites
- Docker & Docker Compose installed
- Java 17+ (for local development)
- Maven 3.6+ (for local development)

### Step 1: Configure Environment
Copy the example environment file and update the secrets:
```bash
cp .env.example .env
```

### Step 2: Run with Docker (Recommended)
Launch the entire stack (Backend, PostgreSQL, Redis) using Docker Compose:
```bash
docker-compose up --build
```
The application will be available at `http://localhost:8080`.

### Step 3: Run Locally (Development)
1. Ensure PostgreSQL and Redis are running on your machine.
2. Navigate to the backend folder:
   ```bash
   cd backend
   ```
3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

## 🔌 API Endpoints

### Health Check
- `GET /health` - Public endpoint to verify service status.

### Authentication (`/api/auth`)
- `POST /api/auth/register` - Register a new user.
- `POST /api/auth/login` - Login and receive a JWT token.

### ESG Records (`/api/esg`) - *Requires JWT Token*
- `GET /api/esg` - Get all records (paginated).
- `GET /api/esg/{id}` - Get a specific record by ID.
- `POST /api/esg` - Create a new ESG record.
- `PUT /api/esg/{id}` - Update an existing record.
- `DELETE /api/esg/{id}` - Delete a record.

## 🔐 Security & Caching
- **JWT Auth:** All `/api/esg/**` endpoints require an `Authorization: Bearer <token>` header.
- **Caching:** GET requests are cached in Redis for 10 minutes. Caches are automatically evicted on POST/PUT/DELETE operations to maintain consistency.
- **Auditing:** All ESG records automatically track `createdAt` and `updatedAt` timestamps.
