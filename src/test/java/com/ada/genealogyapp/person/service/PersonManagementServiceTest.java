package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.person.Gender;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonManagementServiceTest extends IntegrationTestConfig {

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private PersonRepository personRepository;

    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    void shouldUpdatePersonByChangingNameSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person person = new Person("John", "Smith", LocalDate.of(1975, 7, 18), Gender.MALE, tree);
        personRepository.save(person);

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("Adalbert");

        mockMvc.perform(put("/api/v1/genealogy/tree/{treeId}/persons/{personId}", tree.getId(), person.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
