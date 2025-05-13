# Advanced Service-to-Service Communication Guide

This document explains how microservices in our project communicate directly with each other, with a focus on resilience patterns.

## Overview of Service Connections

1. **File Service → Repository Service**
   - The File Service validates repository existence by calling the Repository Service before file uploads
   - This ensures data integrity by preventing file uploads to non-existent repositories
   - Uses **Circuit Breaker** pattern for resilience

2. **Repository Service → User Service**
   - The Repository Service validates user existence by calling the User Service before repository creation
   - This ensures that repositories are only created for valid users and supervisors
   - Uses **Feign Client** with fallback mechanisms

## Resilience Patterns Implemented

### 1. Circuit Breaker Pattern

The Circuit Breaker pattern is implemented in both services using Spring Cloud Circuit Breaker with Resilience4j. This prevents cascading failures when services are down:

- **Closed State**: Requests flow normally to the target service
- **Open State**: If failures exceed threshold, circuit "opens" and calls fail fast without waiting
- **Half-Open State**: After wait duration, allows test requests to check if service recovered

Configuration:
```java
CircuitBreakerConfig.custom()
    .slidingWindowSize(10)            // Sample size for failure rate calculation
    .failureRateThreshold(50)         // 50% of requests must fail to open circuit
    .waitDurationInOpenState(Duration.ofSeconds(10))  // Time before trying again
    .permittedNumberOfCallsInHalfOpenState(5)  // Test calls in half-open state
    .build()
```

### 2. Timeout Management

All service calls have configurable timeouts to prevent hanging threads:

```java
TimeLimiterConfig.custom()
    .timeoutDuration(Duration.ofSeconds(3))  // Requests timeout after 3 seconds
    .build()
```

### 3. Fallback Mechanisms

Both services implement fallbacks for graceful degradation when calls fail:

- **RestTemplate + Circuit Breaker**: File Service uses circuit breaker with fallback to return safe values
- **Feign Client + Fallback Factory**: Repository Service uses FallbackFactory to create dynamic fallbacks

## Implementation Details

### 1. File Service → Repository Service Communication (RestTemplate + Circuit Breaker)

```java
public boolean checkRepositoryExists(Long repositoryId) {
    return repositoryCircuitBreaker.run(() -> {
        // Normal flow: call the repository service
        String url = "http://repository-service/api/repositories/{id}/exists";
        ResponseEntity<Boolean> response = restTemplate.getForEntity(
            url, Boolean.class, repositoryId);
        return Boolean.TRUE.equals(response.getBody());
    }, throwable -> {
        // Fallback: assume repository doesn't exist when service is down
        log.error("Error checking repository existence: {}", throwable.getMessage());
        return false;
    });
}
```

### 2. Repository Service → User Service Communication (Feign + Fallback)

```java
@FeignClient(name = "user-service", fallbackFactory = UserServiceClientFallback.class)
public interface UserServiceClient {
    @GetMapping("/api/users/exists/{id}")
    boolean checkUserExists(@PathVariable Long id);
}

// Fallback implementation
@Component
public class UserServiceClientFallback implements FallbackFactory<UserServiceClient> {
    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {
            @Override
            public boolean checkUserExists(Long id) {
                log.error("Failed to check if user exists: {}", cause.getMessage());
                return false;  // Safe default
            }
        };
    }
}
```

## Configuration Guide

### 1. Enable Circuit Breakers for Feign

```yaml
spring:
  cloud:
    openfeign:
      circuitbreaker:
        enabled: true
```

### 2. Circuit Breaker Configuration

```java
@Bean
public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
    return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
            .timeLimiterConfig(TimeLimiterConfig.custom()
                    .timeoutDuration(Duration.ofSeconds(3))
                    .build())
            .circuitBreakerConfig(CircuitBreakerConfig.custom()
                    .slidingWindowSize(10)
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofSeconds(10))
                    .permittedNumberOfCallsInHalfOpenState(5)
                    .build())
            .build());
}
```

## Testing Resilience Patterns

### Test 1: Service Fail-Open Scenario

1. Start all services
2. Shut down the Repository Service
3. Attempt to upload a file
4. Observe the File Service gracefully handling the failure

### Test 2: Service Recovery

1. Start all services
2. Shut down the User Service
3. Attempt to create a repository (should fail with fallback)
4. Restart the User Service
5. After circuit breaker times out (~10 seconds), retry creating a repository
6. Observe the Repository Service recover and successfully create the repository

## Benefits of Resilient Service Communication

1. **Graceful Degradation**: Services continue functioning even when dependent services fail
2. **Faster Responses**: Failed calls return quickly rather than timing out
3. **Service Recovery**: Circuit breakers automatically test and restore normal operation
4. **Resource Protection**: Prevents thread exhaustion from waiting on failed services
5. **Better User Experience**: Users see reasonable error messages instead of system crashes

## Monitoring Considerations

For production, consider adding monitoring to track circuit breaker states:

1. **Health Endpoints**: Monitor service health including circuit breaker status
2. **Metrics**: Track circuit breaker metrics like success rate, failure rate, etc.
3. **Logging**: Log circuit breaker state changes for troubleshooting
