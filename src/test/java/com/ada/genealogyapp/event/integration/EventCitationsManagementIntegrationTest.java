package com.ada.genealogyapp.event.integration;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EventCitationsManagementIntegrationTest extends IntegrationTestConfig {

    @Autowired
    TreeRepository treeRepository;

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
        citationRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        graphUserRepository.deleteAll();
        treeRepository.deleteAll();
        eventRepository.deleteAll();
        citationRepository.deleteAll();
    }

    @Test
    void shouldAddExistingCitationToEventSuccessfully() throws Exception {

        Event event = new Event();
        eventRepository.save(event);

        Tree tree = new Tree();
        tree.setEvents(Set.of(event));
        treeRepository.save(tree);

        GraphUser user = new GraphUser();
        user.setTrees(Set.of(tree));
        graphUserRepository.save(user);

        CitationRequest citationRequest = new CitationRequest();
        citationRequest.setPage("Page nr 12");
        citationRequest.setDate("2010-04-20");

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/events/{eventId}/citations", tree.getId(), event.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-User-Id", user.getId())
                        .content(objectMapper.writeValueAsString(citationRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

    }
}
