package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventCreationServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    EventService eventService;

    @Mock
    EventValidationService eventValidationService;

    @InjectMocks
    EventCreationService eventCreationService;


    Tree tree;
    UUID treeId;
    EventRequest eventRequest;

    @BeforeEach
    void setUp() {

        treeId = UUID.randomUUID();
        tree = new Tree();
        eventRequest = new EventRequest(EventType.MARRIAGE, LocalDate.parse("2024-11-24"), "place", "description");
    }

    @Test
    void createEvent_shouldCreateEventWhenValidRequest() {

        when(treeService.findTreeById(treeId)).thenReturn(tree);

        Event event = eventCreationService.createEvent(treeId, eventRequest);

        assertEquals("description", event.getDescription());
        assertEquals("place", event.getPlace());
        assertEquals(LocalDate.parse("2024-11-24"), event.getDate());
        assertEquals(EventType.MARRIAGE, event.getType());
        verify(treeService).findTreeById(treeId);
        verify(eventValidationService).validateEvent(event);
        verify(eventService).saveEvent(event);
    }

    @Test
    void createEvent_shouldThrowExceptionWhenTreeDoesNotExist() {

        doThrow(new NodeNotFoundException("Tree not found"))
                .when(treeService).findTreeById(treeId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () ->
                eventCreationService.createEvent(treeId, eventRequest)
        );

        assertEquals("Tree not found", exception.getMessage());
        verify(treeService).findTreeById(treeId);
        verifyNoInteractions(eventValidationService);
        verifyNoInteractions(eventService);
    }

    @Test
    void createEvent_shouldNotSaveEventWhenValidationFails() {

        when(treeService.findTreeById(treeId)).thenReturn(tree);
        doThrow(new ValidationException("Event validation failed"))
                .when(eventValidationService).validateEvent(any(Event.class));

        ValidationException exception = assertThrows(ValidationException.class, () ->
                eventCreationService.createEvent(treeId, eventRequest)
        );

        assertEquals("Event validation failed", exception.getMessage());
        verify(treeService).findTreeById(treeId);
        verify(eventValidationService).validateEvent(any(Event.class));
        verify(eventService, never()).saveEvent(any());
    }
}