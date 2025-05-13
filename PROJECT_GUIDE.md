# Project File Repository - Setup and Troubleshooting Guide

## Project Overview

This project is a file sharing portal for academic projects, built with Spring Boot microservices architecture. The system allows students to upload project files, organize them, and supervisors to review and comment on them.

## Architecture Components

1. **Eureka Server** (Port 8761): Service discovery
2. **API Gateway** (Port 8080): Single entry point for all requests
3. **User Service** (Port 8081): User management and authentication
4. **File Service** (Port 8082): File upload, download, and management
5. **Repository Service** (Port 8083): Project repositories and folder organization

## How to Start the Services

Use the `start-services.bat` script to start all services in the correct order:

```bash
C:\Users\yumenna ezzat\Desktop\project_file_repository\start-services.bat
```

## Authentication Flow

1. Register a new user:
   ```bash
   curl -X 'POST' \
   'http://192.168.1.4:8080/api/v1/auth/register' \
   -H 'accept: /' \
   -H 'Content-Type: application/json' \
   -d '{
     "username": "username",
     "password": "password",
     "email": "email@example.com",
     "fullName": "Full Name",
     "roles": ["Student"]
   }'
   ```

2. Login to get a token:
   ```bash
   curl -X 'POST' \
   'http://192.168.1.4:8080/api/v1/auth/login' \
   -H 'accept: /' \
   -H 'Content-Type: application/json' \
   -d '{
     "username": "username",
     "password": "password"
   }'
   ```

3. Use the token for authenticated requests:
   ```bash
   curl -X 'GET' \
   'http://192.168.1.4:8080/api/repositories/user/1' \
   -H 'accept: /' \
   -H 'Authorization: Bearer YOUR_TOKEN_HERE'
   ```

## Common Issues and Solutions

### 1. API Gateway Not Routing Requests

If the API Gateway isn't routing requests properly:
- Check if all services are registered in Eureka (http://localhost:8761)
- Verify route definitions in the API Gateway's RouteConfig.java
- Ensure service names match between route definitions and service registrations (case-sensitive)

### 2. Authentication Issues

If you're having problems with authentication:
- Ensure JWT secret keys are consistent across services
- Check that auth endpoints are properly defined in the API Gateway
- Verify token format in requests (must be 'Bearer TOKEN')

### 3. CORS Issues

If you're experiencing CORS problems:
- Verify CorsConfig.java in API Gateway allows your client's origin
- Check that preflight OPTIONS requests are handled correctly
- Ensure headers like Authorization are included in allowed headers

### 4. Database Connection Issues

If services can't connect to the database:
- Check database connection strings in application.yml files
- Verify MySQL is running and accessible
- Ensure database users have proper permissions

### 5. Service Discovery Problems

If services can't find each other:
- Ensure Eureka Server is running before other services
- Check that all services have proper Eureka client configuration
- Verify network connectivity between services

## Debugging Tips

1. Increase logging levels for specific components:
   ```yaml
   logging:
     level:
       org.springframework.cloud.gateway: DEBUG
       org.springframework.security: DEBUG
       com.filerepository: DEBUG
   ```

2. Use Eureka dashboard to verify service registration:
   - Open http://localhost:8761 in your browser
   - Check that all services are registered with the expected names

3. Test direct service endpoints first, then through the gateway:
   - User Service: http://localhost:8081/...
   - File Service: http://localhost:8082/...
   - Then try through Gateway: http://localhost:8080/...

## API Documentation

Swagger UI is available for all services:
- API Gateway: http://localhost:8080/swagger-ui.html
- User Service: http://localhost:8081/swagger-ui.html
- File Service: http://localhost:8082/swagger-ui.html
- Repository Service: http://localhost:8083/swagger-ui.html
