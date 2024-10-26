package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.person.dto.PersonRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;

import java.util.HashSet;
import java.util.Set;


class FamilyPersonsManagementIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    FamilyRepository familyRepository;


    @BeforeEach
    void setUp() {
        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        personRepository.deleteAll();
    }

    @Test
    void shouldAddExistingFatherAndExistingChildToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person father = new Person("Adalbert Smith", "Adalbert", "Smith", LocalDate.of(2000, 5, 30), GenderType.MALE, tree);

        Person child = new Person("Claudia", "Smith", LocalDate.of(2020, 4, 9), GenderType.FEMALE, tree);


        Family family = new Family();

        personRepository.save(child);
        personRepository.save(father);
        familyRepository.save(family);

        UUIDRequest UUIDRequest = new UUIDRequest();
        UUIDRequest.setId(father.getId());


        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingFather", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        UUIDRequest.setId(child.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingChild", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isCreated());


        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNotNull(savedFamily.getFather());
        assertEquals(father.getId(), savedFamily.getFather().getId());
    }

    @Test
    void shouldRemoveFatherFromFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person father = new Person("Adalbert Smith", "Adalbert", "Smith", LocalDate.of(2000, 5, 30), GenderType.MALE, tree);

        Person child = new Person("Claudia", "Smith", LocalDate.of(2020, 4, 9), GenderType.FEMALE, tree);


        Family family = new Family();


        personRepository.save(child);
        personRepository.save(father);
        familyRepository.save(family);

        UUIDRequest UUIDRequest = new UUIDRequest();
        UUIDRequest.setId(father.getId());


        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingFather", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        UUIDRequest.setId(child.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingChild", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isCreated());


        UUIDRequest.setId(father.getId());


        mockMvc.perform(delete("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());


        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNull(savedFamily.getFather());
    }

    @Test
    void shouldAddNewFatherToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);


        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("Adalbert");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(2000, 5, 30));
        personRequest.setGenderType(GenderType.MALE);


        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addNewFather", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isCreated());


        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNotNull(savedFamily.getFather());
    }

    @Test
    void shouldAddExistingMotherToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person mother = new Person("Petronella", "Smith", LocalDate.of(2003, 4, 8), GenderType.FEMALE, tree);
        Person child = new Person("Claudia", "Smith", LocalDate.of(2020, 4, 9), GenderType.FEMALE, tree);


        Family family = new Family();

        Set<Person> children = new HashSet<>();
        children.add(child);

        family.setChildren(children);

        personRepository.save(child);
        personRepository.save(mother);
        familyRepository.save(family);

        UUIDRequest UUIDRequest = new UUIDRequest();
        UUIDRequest.setId(mother.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingMother", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNotNull(savedFamily.getMother());
        assertEquals(mother.getId(), savedFamily.getMother().getId());
    }

    @Test
    void shouldAddNewMotherToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        PersonRequest personRequest = new PersonRequest();
        personRequest.setFirstname("Petronella");
        personRequest.setLastname("Smith");
        personRequest.setBirthDate(LocalDate.of(2003, 4, 8));
        personRequest.setGenderType(GenderType.FEMALE);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addNewMother", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNotNull(savedFamily.getMother());
    }

    @Test
    void shouldThrowExceptionWhenMotherAlreadyInFamily() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person mother = new Person("Petronella", "Smith", LocalDate.of(2003, 4, 8), GenderType.FEMALE, tree);

        Family family = new Family();
        family.setMother(mother);

        personRepository.save(mother);
        familyRepository.save(family);

        UUIDRequest UUIDRequest = new UUIDRequest();
        UUIDRequest.setId(mother.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingMother", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isConflict());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertNotNull(savedFamily.getMother());
        assertEquals(mother.getId(), savedFamily.getMother().getId());
    }


    @Test
    void shouldThrowExceptionWhenChildAlreadyInFamily() throws Exception {
        Tree tree = new Tree();
        treeRepository.save(tree);

        Person father = new Person("John", "Smith", LocalDate.of(1975, 7, 18), GenderType.MALE, tree);
        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), GenderType.FEMALE, tree);
        Person child = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), GenderType.FEMALE, tree);

        Set<Person> children = new HashSet<>();
        children.add(child);

        Family family = new Family();
        family.setFather(father);
        family.setMother(mother);
        family.setChildren(children);

        personRepository.save(father);
        personRepository.save(mother);
        personRepository.save(child);
        familyRepository.save(family);

        UUIDRequest UUIDRequest = new UUIDRequest();
        UUIDRequest.setId(child.getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/persons/addExistingChild", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(UUIDRequest)))
                .andDo(print())
                .andExpect(status().isConflict());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertEquals(1, savedFamily.getChildren().size());

    }
}
