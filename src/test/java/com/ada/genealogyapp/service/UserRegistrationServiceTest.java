package com.ada.genealogyapp.service;

import com.ada.genealogyapp.IntegrationTestConfig;
import com.ada.genealogyapp.user.dto.UserRequest;
import com.ada.genealogyapp.user.repository.UserRepository;
import com.ada.genealogyapp.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class UserRegistrationServiceTest extends IntegrationTestConfig {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterUserSuccessfully() throws Exception {

        UserRequest request = new UserRequest();
        request.setFirstname("John");
        request.setLastname("Smith");
        request.setUsername("john.smith@email.com");
        request.setPhone("123456789");
        request.setPassword("password123");
        request.setRole("ROLE_USER");

        mockMvc.perform(post("/api/v1/genealogy/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestForExistingUser() throws Exception {

        User existingUser = new User();
        existingUser.setFirstname("John");
        existingUser.setLastname("Smith");
        existingUser.setUsername("john.smith@email.com");
        existingUser.setPhone("123456789");
        existingUser.setPassword("password123");
        existingUser.setRole("ROLE_USER");
        userRepository.save(existingUser);

        UserRequest request = new UserRequest();
        request.setFirstname("John");
        request.setLastname("Smith");
        request.setUsername("john.smith@email.com");
        request.setPhone("123456789");
        request.setPassword("password123");
        request.setRole("ROLE_USER");


        mockMvc.perform(post("/api/v1/genealogy/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestForNonValidatedUser() throws Exception {

        UserRequest request = new UserRequest();
        request.setFirstname("John$");
        request.setLastname("Smith");
        request.setUsername("john.smith@email.com");
        request.setPhone("123456789");
        request.setPassword("password123");
        request.setRole("ROLE_USER");


        mockMvc.perform(post("/api/v1/genealogy/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

