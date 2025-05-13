package com.filerepository.repositoryservice.client;

import com.filerepository.common.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", fallbackFactory = UserServiceClientFallback.class)
public interface UserServiceClient {
    
    @GetMapping("/api/users/{id}")
    UserDTO getUserById(@PathVariable Long id);

    @GetMapping("/api/users/username/{username}")
    UserDTO getUserByUsername(@PathVariable String username);
    
    @GetMapping("/api/users/exists/{id}")
    boolean checkUserExists(@PathVariable Long id);
}
