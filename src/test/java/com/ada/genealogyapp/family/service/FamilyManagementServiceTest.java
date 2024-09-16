package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FamilyManagementServiceTest extends IntegrationTestConfig {

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private FamilyRepository familyRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
    }

    @Test
    void shouldCreateAndAddPersonToFamilySuccessfully() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(1980, 1, 1));
        personRequest.setGender(Gender.MALE);

        Tree tree = new Tree();
        tree.setUserId(1L);
        tree.setName("Smith Family");
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);


        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families/{familyId}/person/new", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenFamilyDoesNotExist() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(1980, 1, 1));
        personRequest.setGender(Gender.MALE);

        Tree tree = new Tree();
        tree.setUserId(1L);
        tree.setName("Smith Family");
        treeRepository.save(tree);

        UUID nonExistentFamilyId =  UUID.randomUUID();


        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families/{familyId}/person/new", tree.getId(), nonExistentFamilyId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))

                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
