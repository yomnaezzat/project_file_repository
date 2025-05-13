package com.filerepository.fileservice.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RepositoryServiceClient {
    
    private static final Logger log = LoggerFactory.getLogger(RepositoryServiceClient.class);
    private final RestTemplate restTemplate;
    private final CircuitBreaker repositoryCircuitBreaker;
    
    @Autowired
    public RepositoryServiceClient(RestTemplate restTemplate, CircuitBreakerFactory circuitBreakerFactory) {
        this.restTemplate = restTemplate;
        this.repositoryCircuitBreaker = circuitBreakerFactory.create("repositoryService");
    }
    
    /**
     * Checks if a repository exists in the repository service
     * 
     * @param repositoryId the ID of the repository to check
     * @return true if the repository exists, false otherwise
     */
    public boolean checkRepositoryExists(Long repositoryId) {
        return repositoryCircuitBreaker.run(() -> {
            log.info("Checking if repository exists with ID: {}", repositoryId);
            String url = "http://repository-service/api/repositories/{id}/exists";
            
            ResponseEntity<Boolean> response = restTemplate.getForEntity(
                url, 
                Boolean.class, 
                repositoryId
            );
            
            boolean exists = Boolean.TRUE.equals(response.getBody());
            log.info("Repository with ID {} exists: {}", repositoryId, exists);
            return exists;
        }, throwable -> {
            log.error("Error checking repository existence: {}", throwable.getMessage(), throwable);
            // In case of a communication error, fail safely by assuming the repository doesn't exist
            return false;
        });
    }
}
