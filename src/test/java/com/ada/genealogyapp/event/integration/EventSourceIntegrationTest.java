package com.ada.genealogyapp.event.integration;

import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.repository.SourceRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventSourceIntegrationTest extends IntegrationTestConfig {

    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SourceRepository sourceRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        personRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
        sourceRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();


    }

    @Test
    void shouldCreateEventSourceSuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Event event = new Event();
//        event.setEventType(EventType.MARRIAGE);


        eventRepository.save(event);

        System.out.println("OOOOOOOOOOOO"+event);

        SourceRequest sourceRequest = new SourceRequest();

        sourceRequest.setName("Lol");


        mockMvc.perform(post("/api/v1/genealogy/tree/{treeId}/events/{eventId}/sources", tree.getId(), event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sourceRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}
