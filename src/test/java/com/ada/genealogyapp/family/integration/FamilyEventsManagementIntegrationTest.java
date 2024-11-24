package com.ada.genealogyapp.family.integration;


import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FamilyEventsManagementIntegrationTest extends IntegrationTestConfig {


    @Autowired
    TreeRepository treeRepository;

    @Autowired
    FamilyRepository familyRepository;

    @Autowired
    EventRepository eventRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
//
//        treeRepository.deleteAll();
//        familyRepository.deleteAll();
//        eventRepository.deleteAll();

    }

    @Test
    void shouldAddNewEventToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setType(EventType.MARRIAGE);


        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addNewEvent", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

//        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
//        assertEquals(1, savedFamily.getEvents().size());
    }

    @Test
    void shouldAddExistingEventToFamilyFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        Event event = new Event();
        eventRepository.save(event);

        ParticipantEventRequest participantEventRequest = new ParticipantEventRequest();
        participantEventRequest.setId(event.getId());
        participantEventRequest.setEventParticipantRelationshipType(EventParticipantRelationshipType.FAMILY);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addExistingEvent", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(participantEventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

//        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
//        assertEquals(1, savedFamily.getEvents().size());
    }
}
