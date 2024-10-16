package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.Gender;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FamilyManagementServiceTest extends IntegrationTestConfig {

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EventRepository eventRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    void shouldUpdateFamilyByAddingNewChildAndRemovingOldOneSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();

        Person father = new Person("John", "Smith", LocalDate.of(1975, 7, 18), Gender.MALE, tree);
        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), Gender.FEMALE, tree);
        Person firstChild = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), Gender.FEMALE, tree);
        Person secondChild = new Person("Marie", "Smith", LocalDate.of(2012, 5, 30), Gender.FEMALE, tree);

        Set<Person> children = new HashSet<>();
        children.add(firstChild);
        children.add(secondChild);

        family.setFather(father);
        family.setMother(mother);
        family.setChildren(children);

        personRepository.save(father);
        personRepository.save(mother);
        personRepository.save(firstChild);
        personRepository.save(secondChild);

        familyRepository.save(family);

        Person thirdChild = new Person("Adalbert", "Smith", LocalDate.of(2018, 5, 30), Gender.MALE, tree);
        personRepository.save(thirdChild);

        FamilyRequest familyRequest = new FamilyRequest();

        Set<UUID> childrenIds = new HashSet<>();
        childrenIds.add(thirdChild.getId());
        familyRequest.setChildrenIds(childrenIds);

        childrenIds.add(secondChild.getId());
        familyRequest.setChildrenIds(childrenIds);

        mockMvc.perform(put("/api/v1/genealogy/tree/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldUpdateFamilyByRemovingMotherSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();

        Person father = new Person("John", "Smith", LocalDate.of(1975, 7, 18), Gender.MALE, tree);
        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), Gender.FEMALE, tree);
        Person firstChild = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), Gender.FEMALE, tree);
        Person secondChild = new Person("Marie", "Smith", LocalDate.of(2012, 5, 30), Gender.FEMALE, tree);

        Set<Person> children = new HashSet<>();
        children.add(firstChild);
        children.add(secondChild);

        family.setFather(father);
        family.setMother(mother);
        family.setChildren(children);

        personRepository.save(father);
        personRepository.save(mother);
        personRepository.save(firstChild);
        personRepository.save(secondChild);

        familyRepository.save(family);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setMotherId(mother.getId());

        mockMvc.perform(put("/api/v1/genealogy/tree/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
