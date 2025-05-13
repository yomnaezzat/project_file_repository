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
import lombok.RequiredArgsConstructor;
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

    // باقي الميثودات كما هي
}
