package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.exceptions.InvalidCredentialsException;
import com.ada.genealogyapp.exceptions.UserAlreadyExistsException;
import com.ada.genealogyapp.user.dto.UsersResponse;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSearchService {

    private final UserRepository userRepository;


    public Optional<User> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
        } else {
            log.warn("No user found with id: {}", id);
        }
        return user;
    }

    public Optional<User> findUserByUsername(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
        } else {
            log.warn("No user found with username: {}", username);
        }
        return user;
    }

    public User findUserByUsernameOrThrowInvalidCredentials(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
            return user.get();
        } else {
            log.error("Invalid username or password for username: {}", username);
            throw new InvalidCredentialsException("Invalid username or password for username: " + username);
        }
    }

    public void findUserByUsernameOrThrowUserAlreadyExistsException(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.error("User with username {} already exists", username);
            throw new UserAlreadyExistsException("User with username " + username + " already exists");
        } else {
            log.info("User with username {} not found", username);
        }
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.info("Number of users found: {}", users.size());
        return users;
    }

    //TODO paginacja
    public UsersResponse getAllUsers() {
        return UsersResponse.entityToDtoMapper().apply(findAll());
    }
}
