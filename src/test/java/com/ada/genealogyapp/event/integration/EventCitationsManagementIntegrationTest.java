//package com.ada.genealogyapp.event.integration;
//
//import com.ada.genealogyapp.citation.dto.CitationRequest;
//import com.ada.genealogyapp.citation.model.Citation;
//import com.ada.genealogyapp.citation.repository.CitationRepository;
//import com.ada.genealogyapp.config.IntegrationTestConfig;
//import com.ada.genealogyapp.event.model.Event;
//import com.ada.genealogyapp.event.repository.EventRepository;
//import com.ada.genealogyapp.tree.model.Tree;
//import com.ada.genealogyapp.tree.repository.TreeRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
//
//import java.time.LocalDate;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class EventCitationsManagementIntegrationTest extends IntegrationTestConfig {
//
//    @Autowired
//    TreeRepository treeRepository;
//
//    @Autowired
//    EventRepository eventRepository;
//
//    @Autowired
//    CitationRepository citationRepository;
//
//
//    @BeforeEach
//    void setUp() {
//
//        treeRepository.deleteAll();
//        citationRepository.deleteAll();
//        eventRepository.deleteAll();
//
//    }
//
//    @AfterEach
//    void tearDown() {
//
//        treeRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();
//
//    }
//
//    @Test
//    void shouldAddExistingCitationToEventSuccessfully() throws Exception {
//
//        Tree tree = new Tree();
//        treeRepository.save(tree);
//
//        Event event = new Event();
//        eventRepository.save(event);
//
//
//        CitationRequest citationRequest = new CitationRequest();
//        citationRequest.setPage("Page nr 12");
//        citationRequest.setDate(LocalDate.parse("2010-04-20"));
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/events/{eventId}/citations", tree.getId(), event.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(citationRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//        Event savedEvent = eventRepository.findById(event.getId()).orElseThrow();
//        assertEquals(1, savedEvent.getCitations().size());
//        Citation addedCitation = savedEvent.getCitations().iterator().next().getCitation();
//        assertEquals(citationRequest.getPage(), addedCitation.getPage());
//        assertEquals(citationRequest.getDate(), addedCitation.getDate());
//    }
//}
