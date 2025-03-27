package com.ada.genealogyapp.family.integration;

import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.Set;

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

    @Autowired
    GraphUserRepository graphUserRepository;


    @BeforeEach
    void setUp() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
        citationRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
        citationRepository.deleteAll();
    }

    @Test
    void shouldUpdateEventInFamilySuccessfully() throws Exception {

        Event event = new Event();
        event.setType(EventType.BIRTH);
        eventRepository.save(event);

        Family family = new Family();
        familyRepository.save(family);

        Tree tree = new Tree();
        tree.setEvents(Set.of(event));
        tree.setFamilies(Set.of(family));
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        ParticipantEventRequest participantEventRequest = new ParticipantEventRequest();
        participantEventRequest.setRelationship(EventParticipantRelationshipType.FAMILY);
        participantEventRequest.setDescription("Marriage of John Smith and Elizabeth Black");
        participantEventRequest.setDate("2020-05-12");
        participantEventRequest.setPlace("Warsaw");
        participantEventRequest.setType(EventType.MARRIAGE);


        mockMvc.perform(put("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}", tree.getId(), family.getId(), event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(participantEventRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void shouldCreateAddEventToFamilySuccessfully() throws Exception {

        Family family = new Family();
        familyRepository.save(family);

        Tree tree = new Tree();
        tree.setFamilies(Set.of(family));
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        ParticipantEventRequest participantEventRequest = new ParticipantEventRequest();
        participantEventRequest.setRelationship(EventParticipantRelationshipType.FAMILY);
        participantEventRequest.setDescription("Marriage of John Smith and Elizabeth Black");
        participantEventRequest.setDate("2020-05-12");
        participantEventRequest.setPlace("Warsaw");
        participantEventRequest.setType(EventType.MARRIAGE);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(participantEventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }
}