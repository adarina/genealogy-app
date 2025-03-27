package com.ada.genealogyapp.tree.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.user.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class TreeCreationIntegrationTest extends IntegrationTestConfig {

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GraphUserRepository graphUserRepository;


    @BeforeEach
    void setUp() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateTreeSuccessfully() throws Exception {

        GraphUser user = new GraphUser();
        graphUserRepository.save(user);

        TreeRequest request = new TreeRequest();
        request.setName("Smith Family");

        mockMvc.perform(post("/api/v1/genealogy/trees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}