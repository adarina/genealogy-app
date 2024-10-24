package com.ada.genealogyapp.person.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.person.type.GenderType;
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

class PersonCreationIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
    }

    @Test
    void shouldCreatePersonSuccessfully() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(1980, 1, 1));
        personRequest.setGenderType(GenderType.MALE);

        Tree tree = new Tree();
        tree.setUserId(1L);
        tree.setName("Smith Family");
        treeRepository.save(tree);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenTreeDoesNotExist() throws Exception {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(1980, 1, 1));
        personRequest.setGenderType(GenderType.MALE);

        UUID nonExistentTreeId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons", nonExistentTreeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
