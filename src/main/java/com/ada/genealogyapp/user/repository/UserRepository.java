package com.ada.genealogyapp.user.repository;

import com.ada.genealogyapp.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    User findByUsernameAndPassword(String username, String password);
}
