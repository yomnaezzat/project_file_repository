# Microservices Connection Fix

This guide will help you resolve the connection issues between your microservices and the API Gateway.

## Problem Identified

The services were not connecting to the API Gateway due to several issues:

1. Case sensitivity mismatch in service names (API Gateway was using uppercase service names)
2. Missing Eureka client configuration properties
3. Inconsistent configuration formats (mixing YAML and properties)
4. Service discovery and registration issues

## Applied Fixes

1. **Configuration Standardization**:
   - All configuration files converted to YAML format
   - Added explicit Eureka client registration parameters
   - Standardized service naming to lowercase

2. **API Gateway Configuration**:
   - Updated routes to use lowercase service names
   - Added explicit route definitions
   - Increased logging levels for better debugging

3. **Service Configuration**:
   - Added instanceId to make each service instance unique
   - Standardized API paths configuration
   - Ensured consistent Eureka client configuration

4. **Startup Sequence**:
   - Created a startup script to ensure correct service startup order

## How to Run

1. To start all services in the correct order, run:
   ```
   start-services.bat
   ```

2. This script will:
   - Start Eureka Server first
   - Wait for Eureka to initialize
   - Start each microservice in the proper sequence
   - Start API Gateway last

## Testing the Connection

1. After all services are started, verify in Eureka Dashboard:
   - http://localhost:8761

2. You should see all services registered:
   - eureka-server
   - user-service
   - file-service
   - repository-service
   - api-gateway

3. Test API endpoints through the gateway:
   - User Service: http://localhost:8080/api/users/register
   - File Service: http://localhost:8080/api/files
   - Repository Service: http://localhost:8080/api/repositories

## Common Issues and Solutions

1. **Service Not Registering with Eureka**:
   - Check Eureka client configuration
   - Verify service name is consistent (lowercase recommended)
   - Ensure Eureka server is running and accessible

2. **Routes Not Working in API Gateway**:
   - Check API Gateway logs for routing errors
   - Verify route definitions match service names in Eureka
   - Ensure predicates properly match your endpoints

3. **Connection Timeouts**:
   - Increase connection timeouts in Spring Cloud configurations
   - Check for network connectivity issues between services
   - Ensure all services are running on the same network

4. **Authentication Issues**:
   - Verify JWT configuration is consistent across services
   - Check token expiration times
   - Debug authentication filter in API Gateway

## Additional Configuration

For development environments, consider adding these properties to your services:

```yaml
eureka:
  instance:
    lease-renewal-interval-in-seconds: 10
    lease-expiration-duration-in-seconds: 30
  client:
    registry-fetch-interval-seconds: 5
```

This will make service registration and discovery more responsive during development.

## Backing Up Original Configurations

All original configuration files have been backed up to `/backup` folders within each service's resources directory.
