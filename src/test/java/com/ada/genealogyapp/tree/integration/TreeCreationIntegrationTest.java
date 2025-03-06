package com.ada.genealogyapp.tree.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.user.model.User;
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


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateTreeSuccessfully() throws Exception {

        User user = new User();
        user.setId(1L);
        userRepository.save(user);

        TreeRequest request = new TreeRequest();
        request.setName("Smith Family");
        request.setUserId(user.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

//    @Test
//    void shouldReturnBadRequestForExistingTree() throws Exception {
//
//        Tree existingTree = new Tree();
//        existingTree.setName("Smith Family");
//        existingTree.setUserId(1L);
//        treeRepository.save(existingTree);
//
//        TreeRequest request = new TreeRequest();
//        request.setName("Smith Family");
//        request.setUserId(1L);
//
//
//        mockMvc.perform(post("/api/v1/genealogy/trees")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isBadRequest());
//    }
}
