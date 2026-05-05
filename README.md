# ESG Benchmark Comparator

## Overview
The ESG Benchmark Comparator is an AI-powered system that analyzes environmental, social, and governance (ESG) data and generates meaningful insights using a language model.

The system is built using a microservice architecture:
- Java Spring Boot (Backend)
- Flask AI Service (AI Processing)
- Frontend (UI Layer)

---

## Features
- ESG data analysis using AI
- Generate insights and descriptions
- Prompt-based ESG evaluation
- Secure API with input validation
- Rate limiting and security headers
- Docker-based deployment

---

## Architecture
Frontend → Backend → AI Service → Groq LLM → Response

---

## API Endpoints (AI Service)

### Health Check
GET /health

### Generate ESG Insight
POST /ai/generate

### Describe ESG Data
POST /ai/describe

---

## Security
- Input validation and injection protection
- Rate limiting using Flask-Limiter
- Security headers (CSP, X-Frame, etc.)
- OWASP ZAP scan completed (no critical issues)

---

## Testing
- Unit testing using pytest (8 tests passed)
- Manual and edge case testing
- AI output validation

---

## Deployment

### Run locally
```bash
python app.py