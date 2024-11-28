package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ada.genealogyapp.tree.service.TreeService;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;


import java.time.LocalDate;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EventManagementServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    EventService eventService;

    @Mock
    EventValidationService eventValidationService;

    @InjectMocks
    EventManagementService eventManagementService;


    String treeId;
    String eventId;
    Event event;
    EventRequest eventRequest;

    @BeforeEach
    void setUp() {

        treeId = String.valueOf(UUID.randomUUID());
        eventId = String.valueOf(UUID.randomUUID());
        event = new Event();
        eventRequest = new EventRequest(EventType.MARRIAGE, LocalDate.parse("2024-11-24"), "place", "description");
    }

    @Test
    void updateEvent_shouldUpdateEventWhenValidRequest() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(eventService.findEventById(eventId)).thenReturn(event);

        eventManagementService.updateEvent(treeId, eventId, eventRequest);

        assertEquals("description", event.getDescription());
        assertEquals("place", event.getPlace());
        assertEquals(LocalDate.parse("2024-11-24"), event.getDate());
        assertEquals(EventType.MARRIAGE, event.getType());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).findEventById(eventId);
        verify(eventValidationService).validateEvent(event);
        verify(eventService).saveEvent(event);
    }

    @Test
    void updateEvent_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                eventManagementService.updateEvent(treeId, eventId, eventRequest)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(eventValidationService);
        verifyNoInteractions(eventService);
    }

    @Test
    void updateEvent_shouldThrowExceptionWhenEventDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(eventService.findEventById(eventId))
                .thenThrow(new NodeNotFoundException("Event not found"));

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                eventManagementService.updateEvent(treeId, eventId, eventRequest)
        );

        assertEquals("Event not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).findEventById(eventId);
        verifyNoMoreInteractions(eventValidationService);
        verifyNoMoreInteractions(eventService);
    }

    @Test
    void updateEvent_shouldNotSaveEventWhenValidationFails() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(eventService.findEventById(eventId)).thenReturn(event);
        doThrow(new ValidationException("Event validation failed"))
                .when(eventValidationService).validateEvent(event);

        ValidationException exception = assertThrows(ValidationException.class, () ->
                eventManagementService.updateEvent(treeId, eventId, eventRequest)
        );

        assertEquals("Event validation failed", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).findEventById(eventId);
        verify(eventValidationService).validateEvent(event);
        verify(eventService, never()).saveEvent(any());
    }


    @Test
    void deleteEvent_shouldDeleteEventWhenTreeAndEventExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(eventService.findEventById(eventId)).thenReturn(event);
        doNothing().when(eventService).deleteEvent(event);

        eventManagementService.deleteEvent(treeId, eventId);

        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).findEventById(eventId);
        verify(eventService).deleteEvent(event);
    }


    @Test
    void deleteEvent_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).ensureTreeExists(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                eventManagementService.deleteEvent(treeId, eventId)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verifyNoInteractions(eventService);
    }

    @Test
    void deleteEvent_shouldThrowExceptionWhenEventDoesNotExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        when(eventService.findEventById(eventId))
                .thenThrow(new NodeNotFoundException("Event not found"));

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                eventManagementService.deleteEvent(treeId, eventId)
        );

        assertEquals("Event not found", exception.getMessage());
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).findEventById(eventId);
        verify(eventService, never()).deleteEvent(any());
    }
}