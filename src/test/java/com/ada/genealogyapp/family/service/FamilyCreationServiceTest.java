package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FamilyCreationServiceTest extends IntegrationTestConfig {


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
    void shouldCreateFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        tree.setUserId(1L);
        tree.setName("Smith Family");
        treeRepository.save(tree);

        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenTreeDoesNotExist() throws Exception {

        UUID nonExistentTreeId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families", nonExistentTreeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}