package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FamilyCreationIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    GraphUserRepository graphUserRepository;


    @BeforeEach
    void setUp() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @Test
    void shouldCreateFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setStatus(StatusType.MARRIED);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        List<Family> families = familyRepository.findAllFamilies();
        assertEquals(1, families.size());
        assertEquals(StatusType.MARRIED, families.get(0).getStatus());
    }

    @Test
    void shouldThrowValidationExceptionWhenFamilyRelationshipTypeIsNull() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        FamilyRequest familyRequest = new FamilyRequest();

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        List<Family> families = familyRepository.findAllFamilies();
        assertEquals(0, families.size());
    }
}