# How to Run the Microservices Project

This guide explains how to properly run your microservices-based Project File Repository system.

## Understanding the Project Structure

Your project is a **multi-module Maven project** with separate microservices:
- Eureka Server (Discovery Service)
- User Service
- File Service
- Repository Service 
- API Gateway

## Starting the Services

### Option 1: Using the Startup Script (Recommended)

The easiest way to start all services is to use the provided script:

```
C:\Users\yumenna ezzat\Desktop\project_file_repository\start-all-services.bat
```

This script:
- Starts all services in the correct order
- Waits appropriate time between service startups
- Logs output to the logs/ directory
- Properly shuts down all services when you exit

### Option 2: Starting Services Individually

If you need to start services individually (for debugging), use these commands:

```bash
# Start Eureka Server first
cd eureka-server
mvn spring-boot:run

# Then start other services in separate terminals
cd user-service
mvn spring-boot:run

cd file-service
mvn spring-boot:run

cd repository-service
mvn spring-boot:run

cd api-gateway
mvn spring-boot:run
```

### Option 3: Starting from Parent Directory

You can also use the `-pl` flag to specify which module to run:

```bash
# From parent directory
mvn spring-boot:run -pl eureka-server
mvn spring-boot:run -pl user-service
# etc.
```

## Common Errors

### "Unable to find a suitable main class"

This error occurs when you try to run `mvn spring-boot:run` from the parent directory without specifying a module.

**Solution**: Either:
1. Use the startup script
2. Navigate to the specific service directory first
3. Use the `-pl` flag to specify the module

### Connection Issues Between Services

If services can't connect to each other:

1. Check Eureka Dashboard (http://localhost:8761)
2. Ensure all services are registered
3. Check logs for specific error messages

## Testing the API

After all services are running, you can test the endpoints:

```bash
# Register a user
curl -X POST \
  http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "testuser",
    "password": "Password123!",
    "email": "test@example.com",
    "fullName": "Test User",
    "roles": ["Student"]
}'

# Login
curl -X POST \
  http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{
    "username": "testuser",
    "password": "Password123!"
}'
```

Remember to save the JWT token returned from the login endpoint to use in subsequent authorized requests.
