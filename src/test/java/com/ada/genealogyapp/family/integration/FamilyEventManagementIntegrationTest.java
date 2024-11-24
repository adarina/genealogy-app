//package com.ada.genealogyapp.family.integration;
//
//import com.ada.genealogyapp.citation.model.Citation;
//import com.ada.genealogyapp.citation.repository.CitationRepository;
//import com.ada.genealogyapp.config.IntegrationTestConfig;
//import com.ada.genealogyapp.event.dto.EventRequest;
//import com.ada.genealogyapp.event.model.Event;
//import com.ada.genealogyapp.event.repository.EventRepository;
//import com.ada.genealogyapp.event.type.EventType;
//import com.ada.genealogyapp.family.model.Family;
//import com.ada.genealogyapp.family.repostitory.FamilyRepository;
//import com.ada.genealogyapp.tree.model.Tree;
//import com.ada.genealogyapp.tree.repository.TreeRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class FamilyEventManagementIntegrationTest extends IntegrationTestConfig {
//
//    @Autowired
//    TreeRepository treeRepository;
//
//    @Autowired
//    FamilyRepository familyRepository;
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
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();
//
//    }
//
//    @AfterEach
//    void tearDown() {
//
//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();
//
//    }
//
//    @Test
//    void shouldUpdateEventInFamilySuccessfully() throws Exception {
//
//        Tree tree = new Tree();
//        treeRepository.save(tree);
//
//        Family family = new Family();
//        familyRepository.save(family);
//
//        Event event = new Event();
//        event.setType(EventType.BIRTH);
//        eventRepository.save(event);
//
//        EventRequest eventRequest = new EventRequest();
//        eventRequest.setType(EventType.MARRIAGE);
//
//
//        mockMvc.perform(put("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}", tree.getId(), family.getId(), event.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(eventRequest)))
//                .andDo(print())
//                .andExpect(status().isOk());
//
//    }
//
//    @Test
//    void shouldAddExistingSourceToEventInFamilySuccessfully() throws Exception {
//
//        Tree tree = new Tree();
//        treeRepository.save(tree);
//
//        Family family = new Family();
//        familyRepository.save(family);
//
//        Event event = new Event();
//        eventRepository.save(event);
//
//        Citation citation = new Citation();
//        citationRepository.save(citation);
//
//
//        UUIDRequest uuidRequest = new UUIDRequest();
//        uuidRequest.setId(citation.getId());
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}/addExistingCitation", tree.getId(), family.getId(), event.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(uuidRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//    }
//}
