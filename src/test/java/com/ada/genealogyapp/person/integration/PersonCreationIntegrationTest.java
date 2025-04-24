package com.ada.genealogyapp.person.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.person.repository.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PersonCreationIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    GraphUserRepository graphUserRepository;

    @Autowired
    PersonRepository personRepository;


    @BeforeEach
    void setUp() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        personRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void shouldCreatePersonSuccessfully() throws Exception {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setGender(GenderType.MALE);

        Tree tree = new Tree();
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenTreeDoesNotExist() throws Exception {

        GraphUser user = new GraphUser();
        graphUserRepository.save(user);

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("John");
        personRequest.setLastname("Smith");
        personRequest.setGender(GenderType.MALE);

        UUID nonExistentTreeId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons", nonExistentTreeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
