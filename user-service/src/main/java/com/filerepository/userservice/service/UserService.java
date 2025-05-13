package com.filerepository.userservice.service;

import com.filerepository.common.annotation.Audited;
import com.filerepository.common.annotation.LogExecutionTime;
import com.filerepository.common.dto.UserDTO;
import com.filerepository.userservice.dto.AuthRequest;
import com.filerepository.userservice.dto.AuthResponse;
import com.filerepository.userservice.dto.UserRegistrationRequest;
import com.filerepository.userservice.exception.ResourceNotFoundException;
import com.filerepository.userservice.exception.UserAlreadyExistsException;
import com.filerepository.userservice.model.Role;
import com.filerepository.userservice.model.User;
import com.filerepository.userservice.repository.UserRepository;
import com.filerepository.userservice.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Audited(action = "USER_REGISTRATION", resource = "USER")
    @LogExecutionTime
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        Set<Role> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            roles = request.getRoles().stream()
                    .map(role -> Role.valueOf(role.toUpperCase()))
                    .collect(Collectors.toSet());
        } else {
            roles.add(Role.STUDENT); // default
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setRoles(roles);

        return userRepository.save(user);
    }

    @Audited(action = "USER_LOGIN", resource = "USER")
    @LogExecutionTime
    public AuthResponse authenticateUser(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtTokenProvider.createToken(user);

        AuthResponse response = new AuthResponse();
        response.setToken(token);
        response.setUserId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRoles(user.getRoles().stream().map(Role::name).collect(Collectors.toSet()));
        return response;
    }

    @Audited(action = "GET_USER", resource = "USER")
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Audited(action = "GET_USER", resource = "USER")
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Audited(action = "GET_USERS", resource = "USER")
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Audited(action = "GET_SUPERVISORS", resource = "USER")
    public List<UserDTO> getAllSupervisors() {
        return userRepository.findAll().stream()
                .filter(User::isSupervisor)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setRoles(user.getRoles().stream().map(Role::name).collect(Collectors.toSet()));
        return dto;
    }
}
