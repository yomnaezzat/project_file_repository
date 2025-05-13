# Service-to-Service Communication Guide

This document explains how microservices in our project communicate directly with each other, in addition to communication through the API Gateway.

## Overview of Service Connections

1. **File Service → Repository Service**
   - The File Service validates repository existence by calling the Repository Service before file uploads
   - This ensures data integrity by preventing file uploads to non-existent repositories

2. **Repository Service → User Service**
   - The Repository Service validates user existence by calling the User Service before repository creation
   - This ensures that repositories are only created for valid users and supervisors

## Implementation Details

### 1. File Service → Repository Service Communication

When a file is uploaded, the File Service performs the following steps:

1. Receives the upload request with repository ID
2. Calls the Repository Service to verify the repository exists:
   ```java
   boolean repositoryExists = repositoryServiceClient.checkRepositoryExists(repositoryId);
   ```
3. If the repository exists, proceeds with the file upload
4. If the repository doesn't exist, returns an error

This validation prevents file uploads to non-existent repositories, maintaining data integrity across services.

### 2. Repository Service → User Service Communication

When a repository is created, the Repository Service performs the following steps:

1. Receives the repository creation request with owner ID and supervisor IDs
2. Calls the User Service to verify the owner exists:
   ```java
   boolean ownerExists = userServiceClient.checkUserExists(request.getOwnerId());
   ```
3. Calls the User Service to verify each supervisor exists (if any):
   ```java
   for (Long supervisorId : request.getSupervisorIds()) {
       boolean supervisorExists = userServiceClient.checkUserExists(supervisorId);
       // Handle non-existent supervisors
   }
   ```
4. If all users exist, creates the repository
5. If any user doesn't exist, returns an error

## Communication Methods

Our services use two different approaches for service-to-service communication:

1. **RestTemplate** - Used by the File Service to call the Repository Service:
   ```java
   @Bean
   @LoadBalanced
   public RestTemplate restTemplate() {
       return new RestTemplate();
   }
   ```

2. **Feign Client** - Used by the Repository Service to call the User Service:
   ```java
   @FeignClient(name = "user-service")
   public interface UserServiceClient {
       @GetMapping("/api/users/exists/{id}")
       boolean checkUserExists(@PathVariable Long id);
   }
   ```

Both methods leverage Eureka for service discovery, allowing services to find each other dynamically without hardcoded URLs.

## Testing Service Communication

### Test 1: File Upload with Valid Repository

1. Create a repository through the API Gateway:
   ```bash
   curl -X POST 'http://localhost:8080/api/repositories' \
   -H 'Content-Type: application/json' \
   -d '{
     "name": "Test Repository",
     "description": "Test Description",
     "ownerId": 1,
     "supervisorIds": [2]
   }'
   ```

2. Upload a file to the created repository (should succeed):
   ```bash
   curl -X POST 'http://localhost:8080/api/files/upload' \
   -F 'file=@test.txt' \
   -F 'repositoryId=1' \
   -F 'uploaderId=1'
   ```

### Test 2: File Upload with Invalid Repository

1. Attempt to upload a file to a non-existent repository (should fail):
   ```bash
   curl -X POST 'http://localhost:8080/api/files/upload' \
   -F 'file=@test.txt' \
   -F 'repositoryId=999' \
   -F 'uploaderId=1'
   ```

### Test 3: Repository Creation with Valid Users

1. Create a repository with valid owner and supervisors (should succeed):
   ```bash
   curl -X POST 'http://localhost:8080/api/repositories' \
   -H 'Content-Type: application/json' \
   -d '{
     "name": "Valid Repository",
     "description": "Test Description",
     "ownerId": 1,
     "supervisorIds": [2]
   }'
   ```

### Test 4: Repository Creation with Invalid User

1. Attempt to create a repository with non-existent owner (should fail):
   ```bash
   curl -X POST 'http://localhost:8080/api/repositories' \
   -H 'Content-Type: application/json' \
   -d '{
     "name": "Invalid Repository",
     "description": "Test Description",
     "ownerId": 999,
     "supervisorIds": [2]
   }'
   ```

## Benefits of Service-to-Service Communication

1. **Data Integrity**: Ensures consistent data across multiple services
2. **Reduced API Gateway Load**: Not all inter-service communication needs to go through the API Gateway
3. **Fault Isolation**: Each service can handle failures from dependent services gracefully
4. **Improved User Experience**: Early validation prevents issues later in the process

## Potential Challenges

1. **Circuit Breaking**: Consider implementing circuit breakers to handle service outages
2. **Timeout Management**: Configure appropriate timeouts for service calls
3. **Version Compatibility**: Ensure API compatibility when services evolve
