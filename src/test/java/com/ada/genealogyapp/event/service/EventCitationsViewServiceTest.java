package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class EventCitationsViewServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventCitationsViewService eventCitationsViewService;

    UUID treeId;
    UUID eventId;
    UUID citationId;
    Page<EventCitationResponse> mockedPage;
    Pageable pageable;

    @BeforeEach
    void setUp() {

        treeId = UUID.randomUUID();
        eventId = UUID.randomUUID();
        citationId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);
        mockedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Test
    void getEventCitations_shouldReturnCitationsWhenTreeAndEventExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findEventCitations(treeId, eventId, pageable)).thenReturn(mockedPage);

        Page<EventCitationResponse> result = eventCitationsViewService.getEventCitations(treeId, eventId, pageable);

        assertEquals(mockedPage, result);
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findEventCitations(treeId, eventId, pageable);
    }

    @Test
    void getEventCitations_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventCitationsViewService.getEventCitations(treeId, eventId, pageable);
        });

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(eventRepository, eventService);
    }

    @Test
    void getEventCitation_shouldReturnEventCitationWhenExists() {

        EventCitationResponse expectedResponse = new EventCitationResponse();
        expectedResponse.setId(citationId);
        expectedResponse.setPage("Page 1234");
        expectedResponse.setDate(LocalDate.parse("2020-05-20"));

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findEventCitation(treeId, eventId, citationId)).thenReturn(Optional.of(expectedResponse));

        EventCitationResponse result = eventCitationsViewService.getEventCitation(treeId, eventId, citationId);

        assertNotNull(result);
        assertEquals(expectedResponse.getId(), result.getId());
        assertEquals(expectedResponse.getPage(), result.getPage());
        assertEquals(expectedResponse.getDate(), result.getDate());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findEventCitation(treeId, eventId, citationId);
    }

    @Test
    void getEventCitation_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventCitationsViewService.getEventCitation(treeId, eventId, citationId);
        });
        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(eventService, eventRepository);
    }

    @Test
    void getEventCitation_shouldThrowExceptionWhenCitationDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findEventCitation(treeId, eventId, citationId))
                .thenReturn(Optional.empty());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
            eventCitationsViewService.getEventCitation(treeId, eventId, citationId);
        });

        assertEquals("Citation " + citationId + " not found for tree " + treeId + " and event " + eventId, exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findEventCitation(treeId, eventId, citationId);
    }
}