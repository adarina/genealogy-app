package com.ada.genealogyapp.tree.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class TreeCreationIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private TreeRepository treeRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
    }

    @Test
    void shouldCreateTreeSuccessfully() throws Exception {

        TreeRequest request = new TreeRequest();
        request.setName("Smith Family");
        request.setUserId(1L);


        mockMvc.perform(post("/api/v1/genealogy/tree")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnBadRequestForExistingTree() throws Exception {

        Tree existingTree = new Tree();
        existingTree.setName("Smith Family");
        existingTree.setUserId(1L);
        treeRepository.save(existingTree);

        TreeRequest request = new TreeRequest();
        request.setName("Smith Family");
        request.setUserId(1L);


        mockMvc.perform(post("/api/v1/genealogy/tree")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
