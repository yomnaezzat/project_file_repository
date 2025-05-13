package com.filerepository.userservice.controller;

import com.filerepository.common.dto.UserDTO;
import com.filerepository.userservice.dto.AuthRequest;
import com.filerepository.userservice.dto.AuthResponse;
import com.filerepository.userservice.dto.UserRegistrationRequest;
import com.filerepository.userservice.model.User;
import com.filerepository.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for user management operations")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user by their ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setRoles(user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a user by their username")
    public ResponseEntity<UserDTO> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());
        userDTO.setRoles(user.getRoles().stream().map(Enum::name).collect(java.util.stream.Collectors.toSet()));
        return ResponseEntity.ok(userDTO);
    }

    @GetMapping("/exists/{id}")
    @Operation(summary = "Check if user exists", description = "Checks if a user with the given ID exists")
    public ResponseEntity<Boolean> checkUserExists(@PathVariable Long id) {
        boolean exists = userService.checkUserExists(id);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all users", description = "Retrieves all users")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/supervisors")
    @Operation(summary = "Get all supervisors", description = "Retrieves all users with supervisor role")
    public ResponseEntity<List<UserDTO>> getAllSupervisors() {
        List<UserDTO> supervisors = userService.getAllSupervisors();
        return ResponseEntity.ok(supervisors);
    }
}
