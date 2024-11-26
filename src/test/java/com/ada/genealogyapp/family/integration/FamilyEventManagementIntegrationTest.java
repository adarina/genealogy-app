package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FamilyEventManagementIntegrationTest extends IntegrationTestConfig {

    @Autowired
    TreeRepository treeRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    CitationRepository citationRepository;


    @BeforeEach
    void setUp() {

//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();

    }

    @AfterEach
    void tearDown() {

//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();

    }

    @Test
    void shouldUpdateEventInFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        Event event = new Event();
        event.setType(EventType.BIRTH);
        eventRepository.save(event);

        ParticipantEventRequest participantEventRequest = new ParticipantEventRequest();
        participantEventRequest.setRelationship(EventParticipantRelationshipType.FAMILY);
        participantEventRequest.setDescription("Marriage of John Smith and Elizabeth Black");
        participantEventRequest.setDate(LocalDate.parse("2020-05-12"));
        participantEventRequest.setPlace("Warsaw");
        participantEventRequest.setType(EventType.MARRIAGE);


        mockMvc.perform(put("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}", tree.getId(), family.getId(), event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participantEventRequest)))
                .andDo(print())
                .andExpect(status().isOk());

        Event updatedEvent = eventRepository.findById(event.getId()).orElseThrow(() -> new AssertionError("Event not found"));

        assertEquals(EventType.MARRIAGE, updatedEvent.getType());
        assertEquals("Marriage of John Smith and Elizabeth Black", updatedEvent.getDescription());
        assertEquals(LocalDate.parse("2020-05-12"), updatedEvent.getDate());
        assertEquals("Warsaw", updatedEvent.getPlace());
    }

    @Test
    void shouldAddEventToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Person person = new Person();
        person.setTree(tree);

        Family family = new Family();
        family.setTree(tree);
        family.setMother(person);
        familyRepository.save(family);

        Event event = new Event();
        eventRepository.save(event);

        ParticipantEventRequest participantEventRequest = new ParticipantEventRequest();
        participantEventRequest.setRelationship(EventParticipantRelationshipType.FAMILY);
        participantEventRequest.setDescription("Marriage of John Smith and Elizabeth Black");
        participantEventRequest.setDate(LocalDate.parse("2020-05-12"));
        participantEventRequest.setPlace("Warsaw");
        participantEventRequest.setType(EventType.MARRIAGE);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events", tree.getId(), family.getId(), event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participantEventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        //TODO hahaha jak nie ma jak jest :/ dobra nie ma :/
        assertTrue(event.isParticipantAlreadyInEvent(family.getId()));
    }
}
