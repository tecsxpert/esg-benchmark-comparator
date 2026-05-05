# Security Policy

## Security Measures

### JWT Authentication
- The application uses JSON Web Tokens (JWT) for stateless authentication.
- Secrets are managed via environment variables.
- Tokens should be included in the `Authorization: Bearer <token>` header.

### Input Validation
- All API endpoints use Spring Boot's `@Valid` annotation to ensure data integrity.
- Request bodies are validated against predefined DTO constraints.

### SQL Injection Prevention
- Spring Data JPA and Hibernate are used for database operations, which use parameterized queries by default, preventing SQL injection.

### Secure Communication
- In production, ensure the application is served over HTTPS.
