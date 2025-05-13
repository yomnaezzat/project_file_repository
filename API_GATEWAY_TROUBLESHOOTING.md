# API Gateway and Authentication Troubleshooting

## Issue Fixed: 404 Error on `/api/v1/auth/register`

The issue was that your API Gateway configuration had a mismatch between:
- Your Java configuration in `RouteConfig.java` which correctly included the `/api/v1/auth/**` route
- Your YAML configuration in `application.yml` which was missing this route

## Required Changes

I've added the missing route to your `application.yml`:

```yaml
- id: auth-service-public
  uri: lb://user-service
  predicates:
    - Path=/api/v1/auth/**
```

## Testing the Fix

1. Restart all services in the correct order:
   - First, start Eureka Server
   - Next, start the User Service
   - Last, start the API Gateway

2. Use the provided `test-auth-register.bat` script to test the endpoint:
   - This sends a POST request to the register endpoint
   - Includes proper JSON body and Content-Type header
   - Adds verbose output to see exactly what's happening

## Common Mistakes to Avoid

1. **HTTP Method Mismatch**: The register endpoint requires a POST method, not GET
   - The error trace showed `doGet` which suggests you might have tried accessing via GET
   - Always use POST for registration/login endpoints

2. **Content-Type Header**: Ensure you're setting `Content-Type: application/json`
   - Without this, Spring won't know how to parse your request body

3. **Route Configuration Precedence**: In Spring Cloud Gateway, the first matching route wins
   - Keep more specific routes (like `/api/v1/auth/**`) before more general ones

4. **Service Discovery**: Ensure your User Service is registered with Eureka
   - Check Eureka dashboard (http://localhost:8761) to verify

## Additional Troubleshooting

If you still encounter issues, check:

1. **Logs**: Set logging level to DEBUG in all services:
   ```yaml
   logging:
     level:
       org.springframework: DEBUG
       com.filerepository: DEBUG
   ```

2. **Service Health**: Verify User Service is running properly:
   - Try accessing it directly at http://localhost:8081/actuator/health
   - Ensure it responds with {"status":"UP"}

3. **Gateway Routes**: Check if routes are registered correctly:
   - Access http://localhost:8080/actuator/gateway/routes
   - Ensure your auth route appears in the list

4. **Request Validation**: If registration fails with 400 Bad Request:
   - Check the request format matches the expected DTO fields
   - Ensure all required fields are present
   - Verify password meets any complexity requirements

## Best Practices

1. **Keep Configuration Consistent**: Ensure your Java and YAML configurations match
2. **Use Spring Boot Actuator**: Enable it for health checks and monitoring
3. **Test Endpoints Directly First**: Verify services work before testing through gateway
4. **Follow Service Order**: Always start Eureka first, API Gateway last
