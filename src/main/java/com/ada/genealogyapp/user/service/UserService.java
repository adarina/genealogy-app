package com.ada.genealogyapp.user.service;

import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserSearchService userSearchService;

    public UserService(UserRepository userRepository, UserSearchService userSearchService) {
        this.userRepository = userRepository;
        this.userSearchService = userSearchService;
    }

    @Transactional
    public boolean deleteUserByUsername(String username) {
        Optional<User> user = userSearchService.find(username);
        if (user.isPresent()) {
            delete(user.get().getUsername());
            log.info("User with Username {} deleted successfully", username);
            return true;
        } else {
            log.warn("User with Username {} not found", username);
            return false;
        }
    }

    @Transactional("jpaTransactionManager")
    public User create(User user) {
        User savedUser = userRepository.save(user);
        log.info("User created successfully: {}", savedUser);
        return savedUser;
    }

    @Transactional
    public void delete(String username) {
        userRepository.deleteByUsername(username);
    }
}
