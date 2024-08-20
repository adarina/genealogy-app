package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.user.dto.*;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.repository.UserRepository;
import com.ada.genealogyapp.user.validation.*;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.core.GrantedAuthority;

import java.util.stream.Collectors;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;

    @Value("${secret.key}")
    private String jwtSecretKey;

    public UserService(UserRepository userRepository, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userValidator = UserValidator.link(
                new UsernameUserValidator(),
                new LastnameUserValidator(),
                new PasswordUserValidator(),
                new PhoneUserValidator(),
                new FirstnameUserValidator()
        );
    }

    public Optional<User> find(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.debug("User found: {}", user.get());
        } else {
            log.warn("No user found with ID: {}", id);
        }
        return user;
    }

    public Optional<User> find(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.debug("User found: {}", user.get());
        } else {
            log.warn("No user found with username: {}", username);
        }
        return user;
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.debug("Number of users found: {}", users.size());
        return users;
    }

    public GetUsersResponse getAllUsers() {
        return GetUsersResponse.entityToDtoMapper().apply(findAll());
    }

    public Optional<GetUserResponse> getUserById(Long id) {
        return find(id).map(GetUserResponse.entityToDtoMapper());
    }

    public boolean validateUser(User user) {
        boolean isValid = userValidator.check(user);
        if (!isValid) {
            log.error("User validation failed: {}", user);
        } else {
            log.debug("User validation passed: {}", user);
        }
        return isValid;
    }

    public ResponseEntity<Void> registerUser(CreateUserRequest request, UriComponentsBuilder builder) {
        User user = CreateUserRequest.dtoToEntityMapper().apply(request);

        if (find(user.getUsername()).isPresent()) {
            log.warn("User with username {} already exists", user.getUsername());
            return ResponseEntity.notFound().build();
        }

        if (!validateUser(user)) {
            log.error("User validation failed for registration: {}", user);
            throw new IllegalArgumentException("User validation failed.");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(hashedPassword);
        user = create(user);
        log.info("User registered successfully: {}", user.getUsername());
        return ResponseEntity.created(builder.pathSegment("api", "v1", "genealogy", "{username}")
                .buildAndExpand(user.getUsername()).toUri()).build();
    }

    public ResponseEntity<Void> deleteUserById(Long id) {
        Optional<User> user = find(id);
        if (user.isPresent()) {
            delete(user.get().getId());
            log.info("User with ID {} deleted successfully", id);
            return ResponseEntity.accepted().build();
        } else {
            log.warn("User with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<?> loginUser(CreateLoginRequest createLoginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(createLoginRequest.getUsername(), createLoginRequest.getPassword()));

            User user = (User) authentication.getPrincipal();
            log.debug("Authentication successful for user: {}", user.getUsername());

            Algorithm algorithm = Algorithm.HMAC256(jwtSecretKey);
            String token = JWT.create()
                    .withSubject(user.getUsername())
                    .withIssuer("Ada")
                    .withClaim("roles", user.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .collect(Collectors.toList()))
                    .sign(algorithm);

            log.info("JWT token generated successfully for user: {}", user.getUsername());

            GetLoginResponse loginResponse = new GetLoginResponse(user.getUsername(), token, user.getId(), user.getRole());
            return ResponseEntity.ok(loginResponse);
        } catch (UsernameNotFoundException exception) {
            log.error("Login failed for user: {}", createLoginRequest.getUsername(), exception);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    public User create(User user) {
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser);
        return savedUser;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("User with ID {} deleted successfully", id);
    }

    public User save(User user) {
        User savedUser = userRepository.save(user);
        log.info("User saved successfully: {}", savedUser);
        return savedUser;
    }

    public User findByUsernameAndPassword(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            log.debug("User found: {}", user);
        } else {
            log.warn("User not found with username: {}", username);
        }
        return user;
    }
}
