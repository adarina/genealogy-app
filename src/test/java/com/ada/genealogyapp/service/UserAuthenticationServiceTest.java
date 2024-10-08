package com.ada.genealogyapp.service;

import com.ada.genealogyapp.IntegrationTestConfig;
import com.ada.genealogyapp.user.dto.LoginRequest;
import com.ada.genealogyapp.user.model.User;
import com.ada.genealogyapp.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class UserAuthenticationServiceTest extends IntegrationTestConfig {

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        User user = new User();
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setUsername("john.smith@email.com");
        user.setPhone("123456789");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john.smith@email.com");
        loginRequest.setPassword("password123");

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenReturn(authentication);

        mockMvc.perform(post("/api/v1/genealogy/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("john.smith@email.com"))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestForBadPassword() throws Exception {

        User user = new User();
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setUsername("john.smith@email.com");
        user.setPhone("123456789");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john.smith@email.com");
        loginRequest.setPassword("wrongPassword");

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenThrow(new BadCredentialsException("Invalid username or password"));


        mockMvc.perform(post("/api/v1/genealogy/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForNonExistingUser() throws Exception {

        User user = new User();
        user.setFirstname("John");
        user.setLastname("Smith");
        user.setUsername("john.smith@email.com");
        user.setPhone("123456789");
        user.setPassword(new BCryptPasswordEncoder().encode("password123"));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("john.rolf@email.com");
        loginRequest.setPassword("password123");

        when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())))
                .thenThrow(new BadCredentialsException("Invalid username or password"));


        mockMvc.perform(post("/api/v1/genealogy/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
