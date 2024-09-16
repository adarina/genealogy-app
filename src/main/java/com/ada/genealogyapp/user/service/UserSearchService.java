package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.user.dto.UsersResponse;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class UserSearchService {

    private final UserRepository userRepository;

    public UserSearchService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> find(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
        } else {
            log.warn("No user found with ID: {}", id);
        }
        return user;
    }


    public Optional<User> find(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
        } else {
            log.warn("No user found with username: {}", username);
        }
        return user;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        log.info("Number of users found: {}", users.size());
        return users;
    }

    public UsersResponse getAllUsers() {
        return UsersResponse.entityToDtoMapper().apply(findAll());
    }
}
