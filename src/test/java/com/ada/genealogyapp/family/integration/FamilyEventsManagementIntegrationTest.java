package com.ada.genealogyapp.family.integration;


import com.ada.genealogyapp.config.IntegrationTestConfig;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.family.dto.FamilyEventRequest;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FamilyEventsManagementIntegrationTest extends IntegrationTestConfig {


    @Autowired
    private TreeRepository treeRepository;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private EventRepository eventRepository;


    @BeforeEach
    void setUp() {

        treeRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {

        treeRepository.deleteAll();
        familyRepository.deleteAll();
        eventRepository.deleteAll();

    }

    @Test
    void shouldAddExistingEventToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        Event event = new Event();
        eventRepository.save(event);

        FamilyEventRequest familyEventRequest = new FamilyEventRequest();
        familyEventRequest.setId(event.getId());
        familyEventRequest.setEventRelationshipType(EventRelationshipType.FAMILY);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addExistingEvent", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(familyEventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/commit", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertEquals(1, savedFamily.getEvents().size());
        assertEquals(event.getId(), savedFamily.getEvents().iterator().next().getEvent().getId());
    }

    @Test
    void shouldAddNewEventToFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setType(EventType.MARRIAGE);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addNewEvent", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/commit", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertEquals(1, savedFamily.getEvents().size());
    }

    @Test
    void shouldAnnNewEventToFamilyAndRemoveItFromFamilySuccessfully() throws Exception {

        Tree tree = new Tree();
        treeRepository.save(tree);

        Family family = new Family();
        familyRepository.save(family);

        EventRequest eventRequest = new EventRequest();
        eventRequest.setType(EventType.MARRIAGE);

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events/addNewEvent", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/commit", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        Family savedFamily = familyRepository.findById(family.getId()).orElseThrow();

        UUIDRequest uuidRequest = new UUIDRequest();
        uuidRequest.setId(savedFamily.getEvents().iterator().next().getEvent().getId());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/families/{familyId}", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/genealogy/trees/{treeId}/families/{familyId}/events", tree.getId(), family.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(uuidRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        mockMvc.perform(post("/api/v1/genealogy/trees/{treeId}/commit", tree.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());

        savedFamily = familyRepository.findById(family.getId()).orElseThrow();
        assertEquals(0, savedFamily.getEvents().size());
    }
}
