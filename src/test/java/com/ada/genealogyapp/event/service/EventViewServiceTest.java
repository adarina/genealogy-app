package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ada.genealogyapp.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.ada.genealogyapp.tree.service.TreeService;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class EventViewServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    EventViewService eventViewService;

    UUID treeId;
    UUID eventId;
    Pageable pageable;
    String filter;

    @BeforeEach
    void setUp() {

        treeId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        filter = "{\"description\":\"Baptism of John Smith\",\"participants\":\"John Smith\",\"type\":\"MARRIAGE\"}";
        pageable = PageRequest.of(0, 10);
    }

    @Test
    void getEvents_shouldReturnEventsWhenValidFilter() throws JsonProcessingException {

        EventFilterRequest filterRequest = new EventFilterRequest("Baptism of John Smith", "John Smith", "MARRIAGE");
        Page<EventPageResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, EventFilterRequest.class)).thenReturn(filterRequest);
        when(eventRepository.findByTreeIdAndFilteredDescriptionParticipantNamesAndType(
                treeId, "Baptism of John Smith", "John Smith", "MARRIAGE", pageable
        )).thenReturn(expectedPage);

        Page<EventPageResponse> result = eventViewService.getEvents(treeId, filter, pageable);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(treeService).ensureTreeExists(treeId);
        verify(objectMapper).readValue(filter, EventFilterRequest.class);
        verify(eventRepository).findByTreeIdAndFilteredDescriptionParticipantNamesAndType(
                treeId, "Baptism of John Smith", "John Smith", "MARRIAGE", pageable);
    }

    @Test
    void getEvents_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventViewService.getEvents(treeId, filter, pageable);
        });

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(objectMapper, eventRepository);
    }

    @Test
    void getEvent_shouldReturnEventWhenExists() {

        UUID participantId = UUID.randomUUID();
        UUID citationId = UUID.randomUUID();

        EventResponse expectedResponse = new EventResponse();
        expectedResponse.setId(eventId);
        expectedResponse.setType(EventType.MARRIAGE);
        expectedResponse.setDescription("Marriage of John Smith & Elizabeth Black");
        expectedResponse.setDate(LocalDate.parse("2020-05-20"));
        expectedResponse.setPlace("Warsaw");

        LinkedHashSet<EventParticipantResponse> participants = new LinkedHashSet<>();
        participants.add(new EventParticipantResponse(participantId, "Ella Black", EventParticipantRelationshipType.PRIEST));
        expectedResponse.setParticipants(participants);

        LinkedHashSet<EventCitationResponse> citations = new LinkedHashSet<>();
        citations.add(new EventCitationResponse(citationId, "Birth certificate nr 4", LocalDate.parse("1980-05-20")));
        expectedResponse.setCitations(citations);

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findByTreeIdAndEventId(treeId, eventId)).thenReturn(Optional.of(expectedResponse));

        EventResponse result = eventViewService.getEvent(treeId, eventId);

        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getType(), result.getType());
        assertEquals(expectedResponse.getDescription(), result.getDescription());
        assertEquals(expectedResponse.getDate(), result.getDate());
        assertEquals(expectedResponse.getPlace(), result.getPlace());
        assertEquals(expectedResponse.getParticipants(), result.getParticipants());
        assertEquals(expectedResponse.getCitations(), result.getCitations());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findByTreeIdAndEventId(treeId, eventId);
    }

    @Test
    void getEvent_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventViewService.getEvent(treeId, eventId);
        });
        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(eventService, eventRepository);
    }

    @Test
    void getEvent_shouldThrowExceptionWhenEventDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findByTreeIdAndEventId(treeId, eventId))
                .thenReturn(Optional.empty());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventViewService.getEvent(treeId, eventId);
        });

        assertEquals("Event " + eventId + " not found for tree " + treeId, exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findByTreeIdAndEventId(treeId, eventId);
    }
}


