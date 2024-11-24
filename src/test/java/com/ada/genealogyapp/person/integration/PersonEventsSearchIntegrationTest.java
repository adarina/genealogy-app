//package com.ada.genealogyapp.person.integration;
//
//import com.ada.genealogyapp.citation.model.Citation;
//import com.ada.genealogyapp.citation.repository.CitationRepository;
//import com.ada.genealogyapp.config.IntegrationTestConfig;
//import com.ada.genealogyapp.event.model.Event;
//import com.ada.genealogyapp.event.repository.EventRepository;
//import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
//import com.ada.genealogyapp.event.type.EventType;
//import com.ada.genealogyapp.family.repostitory.FamilyRepository;
//import com.ada.genealogyapp.person.model.Person;
//import com.ada.genealogyapp.person.repostitory.PersonRepository;
//import com.ada.genealogyapp.person.type.GenderType;
//import com.ada.genealogyapp.tree.model.Tree;
//import com.ada.genealogyapp.tree.repository.TreeRepository;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class PersonEventsSearchIntegrationTest extends IntegrationTestConfig {
//
//
//    @Autowired
//    TreeRepository treeRepository;
//
//    @Autowired
//    PersonRepository personRepository;
//
//    @Autowired
//    EventRepository eventRepository;
//
//    @Autowired
//    FamilyRepository familyRepository;
//
//    @Autowired
//    CitationRepository citationRepository;
//
//    @BeforeEach
//    void setUp() {
//
//        treeRepository.deleteAll();
//        personRepository.deleteAll();
//        eventRepository.deleteAll();
//        citationRepository.deleteAll();
//        familyRepository.deleteAll();
//    }
//
//    @AfterEach
//    void tearDown() {
////        treeRepository.deleteAll();
////        personRepository.deleteAll();
////        eventRepository.deleteAll();
////        citationRepository.deleteAll();
////        familyRepository.deleteAll();
//    }
//
//
//    @Test
//    void shouldReturnAllEventsForPersonSuccessfully() throws Exception {
//
//        Tree tree = new Tree();
//        tree.setName("LOL");
//        treeRepository.save(tree);
//
//        Person person = new Person("John Smith", "John", "Smith", LocalDate.of(1975, 7, 18), GenderType.MALE, tree);
//        personRepository.save(person);
//
//        Person secondPerson = new Person("Mary Sue", "Mary", "Sue", LocalDate.of(1999, 12, 1), GenderType.FEMALE, tree);
//        personRepository.save(secondPerson);
//
////        Family family = new Family();
////        family.setName("Rick Smith & Theodora Smith");
////        familyRepository.save(family);
//
//        Citation citation = new Citation();
//        citation.setPage("status");
//        citation.setTree(tree);
//        citationRepository.save(citation);
//
//        Event event = new Event(EventType.BAPTISM, LocalDate.of(1975, 7, 18), "New York", "Birth event", tree);
//
////        CitationLol status = new CitationLol(myCitation, EventRelationshipType.FAMILY);
////        event.setCitations(Set.of(status));
//
//
//        Event secondEvent = new Event(EventType.BIRTH, LocalDate.of(1975, 7, 20), "California", "Baptism event", tree);
//
//        eventRepository.saveAll(List.of(event, secondEvent));
//
//        PersonUpdateRequest personEventRequest1 = new PersonUpdateRequest();
//        personEventRequest1.setId(event.getId());
//        personEventRequest1.setEventParticipantRelationshipType(EventParticipantRelationshipType.MAIN);
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons/{personId}/events/addExistingEvent", tree.getId(), person.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(personEventRequest1)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//        PersonUpdateRequest personEventRequest3 = new PersonUpdateRequest();
//        personEventRequest3.setId(secondEvent.getId());
//        personEventRequest3.setEventParticipantRelationshipType(EventParticipantRelationshipType.PRIEST);
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons/{personId}/events/addExistingEvent", tree.getId(), person.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(personEventRequest3)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//
//        PersonUpdateRequest personEventRequest = new PersonUpdateRequest();
//        personEventRequest.setId(event.getId());
//        personEventRequest.setEventParticipantRelationshipType(EventParticipantRelationshipType.PRIEST);
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/persons/{personId}/events/addExistingEvent", tree.getId(), secondPerson.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(personEventRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
//        PersonUpdateRequest personEventRequestLOL = new PersonUpdateRequest();
//        personEventRequestLOL.setId(citation.getId());
//        personEventRequestLOL.setEventParticipantRelationshipType(EventParticipantRelationshipType.FAMILY);
//
//        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/citations/{citationId}/events/addExistingEvent", tree.getId(), citation.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(personEventRequest)))
//                .andDo(print())
//                .andExpect(status().isCreated());
//
////        FamilyEventRequest familyEventRequest = new FamilyEventRequest();
////        familyEventRequest.setId(event.getId());
////        familyEventRequest.setEventRelationshipType(EventRelationshipType.FAMILY);
////
////        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addExistingEvent", tree.getId(), family.getId())
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content(objectMapper.writeValueAsString(personEventRequest)))
////                .andDo(print())
////                .andExpect(status().isCreated());
//
//
////        mockMvc.perform(get("/api/v1/genealogy/trees/{treeId}/events", tree.getId())
////                        .contentType(MediaType.APPLICATION_JSON))
////                .andDo(print())
////                .andExpect(status().isOk());
//
//    }
//
//
//
//}
