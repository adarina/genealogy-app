package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.type.GenderType;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FamilyCreationIntegrationTest extends IntegrationTestConfig {


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

//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();


    }

    @Test
    void shouldCreateFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person father = new Person("John", "Smith", LocalDate.of(1975, 7, 18), GenderType.MALE, tree);
        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), GenderType.FEMALE, tree);
        Person firstChild = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), GenderType.FEMALE, tree);
        Person secondChild = new Person("Marie", "Smith", LocalDate.of(2012, 5, 30), GenderType.FEMALE, tree);

        personRepository.save(father);
        personRepository.save(mother);
        personRepository.save(firstChild);
        personRepository.save(secondChild);

        Event marriage = new Event(EventType.MARRIAGE, LocalDate.of(200,3,12), "Poland", "Marriage", tree);

        eventRepository.save(marriage);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setFatherId(father.getId());
        familyRequest.setMotherId(mother.getId());

        Set<UUID> childrenIds = new HashSet<>();
        childrenIds.add(firstChild.getId());
        childrenIds.add(secondChild.getId());

        familyRequest.setChildrenIds(childrenIds);

        Set<UUID> eventsIds = new HashSet<>();
        eventsIds.add(marriage.getId());

        familyRequest.setEventsIds(eventsIds);

        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldCreateFamilySuccessfullyWhenFatherIsNull() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), GenderType.FEMALE, tree);
        Person firstChild = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), GenderType.FEMALE, tree);
        Person secondChild = new Person("Marie", "Smith", LocalDate.of(2012, 5, 30), GenderType.FEMALE, tree);
        personRepository.save(mother);
        personRepository.save(firstChild);
        personRepository.save(secondChild);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setFatherId(null);
        familyRequest.setMotherId(mother.getId());

        Set<UUID> childrenIds = new HashSet<>();
        childrenIds.add(firstChild.getId());
        childrenIds.add(secondChild.getId());

        familyRequest.setChildrenIds(childrenIds);

        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnNotFoundWhenTreeDoesNotExist() throws Exception {

        Person father = new Person("John", "Smith", LocalDate.of(1975, 7, 18), GenderType.MALE, null);
        Person mother = new Person("Anne", "Cleves", LocalDate.of(1975, 10, 10), GenderType.FEMALE, null);
        Person firstChild = new Person("Claudia", "Smith", LocalDate.of(2004, 4, 9), GenderType.FEMALE, null);
        Person secondChild = new Person("Marie", "Smith", LocalDate.of(2012, 5, 30), GenderType.FEMALE, null);

        personRepository.save(father);
        personRepository.save(mother);
        personRepository.save(firstChild);
        personRepository.save(secondChild);

        FamilyRequest familyRequest = new FamilyRequest();
        familyRequest.setFatherId(father.getId());
        familyRequest.setMotherId(mother.getId());

        Set<UUID> childrenIds = new HashSet<>();
        childrenIds.add(firstChild.getId());
        childrenIds.add(secondChild.getId());

        familyRequest.setChildrenIds(childrenIds);

        UUID nonExistentTreeId = UUID.randomUUID();

        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/families", nonExistentTreeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}