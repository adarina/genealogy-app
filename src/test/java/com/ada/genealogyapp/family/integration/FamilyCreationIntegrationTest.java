package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.family.type.RelationshipRelationshipType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FamilyCreationIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    FamilyRepository familyRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        familyRepository.deleteAll();

    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        familyRepository.deleteAll();

    }

    @Test
    void shouldCreateFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setRelationshipRelationshipType(RelationshipRelationshipType.MARRIED);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        List<Family> families = familyRepository.findAll();
        assertEquals(1, families.size());
        assertEquals(RelationshipRelationshipType.MARRIED, families.get(0).getRelationshipRelationshipType());
    }

    @Test
    void shouldCreateFamilySuccessfullyWhenFamilyRelationshipTypeINull() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        FamilyRequest familyRequest = new FamilyRequest();


        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        List<Family> families = familyRepository.findAll();
        assertEquals(1, families.size());
        assertEquals(RelationshipRelationshipType.UNKNOWN, families.get(0).getRelationshipRelationshipType());
    }
}