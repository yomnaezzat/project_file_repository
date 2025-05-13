package com.filerepository.repositoryservice.client;

import com.filerepository.common.dto.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class UserServiceClientFallback implements FallbackFactory<UserServiceClient> {
    
    private static final Logger log = LoggerFactory.getLogger(UserServiceClientFallback.class);
    
    @Override
    public UserServiceClient create(Throwable cause) {
        return new UserServiceClient() {
            @Override
            public UserDTO getUserById(Long id) {
                log.error("Failed to get user by ID: {}, reason: {}", id, cause.getMessage(), cause);
                // Return a minimal DTO with just the ID in case of failure
                UserDTO fallbackUser = new UserDTO();
                fallbackUser.setId(id);
                fallbackUser.setUsername("unknown-user");
                fallbackUser.setFullName("Unknown User");
                return fallbackUser;
            }

            @Override
            public UserDTO getUserByUsername(String username) {
                log.error("Failed to get user by username: {}, reason: {}", username, cause.getMessage(), cause);
                // Return a minimal DTO with just the username in case of failure
                UserDTO fallbackUser = new UserDTO();
                fallbackUser.setUsername(username);
                fallbackUser.setFullName("Unknown User");
                return fallbackUser;
            }

            @Override
            public boolean checkUserExists(Long id) {
                log.error("Failed to check if user exists with ID: {}, reason: {}", id, cause.getMessage(), cause);
                // In case of failure, default to false (fail safe)
                return false;
            }
        };
    }
}
