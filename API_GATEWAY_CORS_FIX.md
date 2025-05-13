# API Gateway CORS and Routing Fix

This document explains the changes made to fix CORS issues and enable access to all microservices through the API Gateway.

## Changes Made

### 1. API Gateway CORS Configuration

- Added global CORS configuration in `application.yml`
- Updated `CorsConfig.java` to allow all origins
- Added a new `CorsFilter.java` that handles OPTIONS requests properly

### 2. API Gateway Route Configuration

- Added a new route for `/api/v1/auth/**` endpoints
- Ensured all routes use lowercase service names for consistency

### 3. User Service CORS Configuration

- Added `CorsConfig.java` to enable CORS for all origins
- Added `WebConfig.java` to configure CORS at the Spring MVC level

## How to Test the Changes

After applying these changes, restart all services and try the following:

### Register a User via API Gateway

```bash
curl -X 'POST' \
'http://192.168.1.4:8080/api/v1/auth/register' \
-H 'accept: /' \
-H 'Content-Type: application/json' \
-d '{
"username": "esraa345",
"password": "str1234!@",
"email": "rehab@gmail.com",
"fullName": "string",
"roles": [
"Supervisor"
]
}'
```

### Login via API Gateway

```bash
curl -X 'POST' \
'http://192.168.1.4:8080/api/v1/auth/login' \
-H 'accept: /' \
-H 'Content-Type: application/json' \
-d '{
"username": "esraa345",
"password": "str1234!@"
}'
```

## Troubleshooting

If you still encounter CORS issues:

1. Check browser console for specific error messages
2. Verify all services are running and registered with Eureka
3. Try accessing the endpoints directly from the service first, then through the gateway
4. Ensure your client application is sending the correct HTTP headers
5. Try disabling any browser security extensions that might block CORS requests

## Additional Configuration

For production environments, you should:

1. Replace `*` with specific allowed origins
2. Configure more restrictive CORS policies
3. Add rate limiting and security headers
