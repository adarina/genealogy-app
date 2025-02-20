//package com.ada.genealogyapp.event.service;
//
//import com.ada.genealogyapp.citation.service.CitationService;
//import com.ada.genealogyapp.event.model.Event;
//import com.ada.genealogyapp.citation.model.Citation;
//import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
//import com.ada.genealogyapp.exceptions.NodeNotFoundException;
//import com.ada.genealogyapp.relationship.RelationshipManager;
//import com.ada.genealogyapp.tree.service.TreeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//import static org.mockito.Mockito.verify;
//
//@ExtendWith(MockitoExtension.class)
//class EventCitationManagementServiceTest {
//
//    @Mock
//    TreeService treeService;
//
//    @Mock
//    EventService eventService;
//
//    @Mock
//    CitationService citationService;
//
//    @Mock
//    RelationshipManager relationshipManager;
//
//    @InjectMocks
//    EventCitationManagementService eventCitationManagementService;
//
//    String treeId;
//    String eventId;
//    String citationId;
//    Event event;
//    Citation citation;
//
//    @BeforeEach
//    void setUp() {
//        treeId = String.valueOf(UUID.randomUUID());
//        eventId = String.valueOf(UUID.randomUUID());
//        citationId = String.valueOf(UUID.randomUUID());
//
//        event = new Event();
//        event.setId(eventId);
//        citation = new Citation();
//        citation.setId(citationId);
//    }
//
//    @Test
//    void removeCitationFromEvent_shouldRemoveCitationWhenValidInputs() {
//
//        doNothing().when(treeService).ensureTreeExists(treeId);
//        when(eventService.findEventById(eventId)).thenReturn(event);
//        when(citationService.findCitationById(citationId)).thenReturn(citation);
//
//        eventCitationManagementService.removeCitationFromEvent(treeId, eventId, citationId);
//
//        verify(treeService).ensureTreeExists(treeId);
//        verify(eventService).findEventById(eventId);
//        verify(citationService).findCitationById(citationId);
//        verify(relationshipManager).removeEventCitationRelationship(event, citation);
//    }
//
//    @Test
//    void removeCitationFromEvent_shouldThrowExceptionWhenTreeDoesNotExist() {
//
//        doThrow(new NodeNotFoundException("Tree not found"))
//                .when(treeService).ensureTreeExists(treeId);
//
//        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
//            eventCitationManagementService.removeCitationFromEvent(treeId, eventId, citationId);
//        });
//        assertEquals("Tree not found", exception.getMessage());
//        verifyNoInteractions(eventService, citationService, relationshipManager);
//    }
//
//    @Test
//    void removeCitationFromEvent_shouldThrowExceptionWhenEventDoesNotExist() {
//
//        doNothing().when(treeService).ensureTreeExists(treeId);
//        when(eventService.findEventById(eventId))
//                .thenThrow(new NodeNotFoundException("Event not found"));
//
//        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
//            eventCitationManagementService.removeCitationFromEvent(treeId, eventId, citationId);
//        });
//        assertEquals("Event not found", exception.getMessage());
//        verify(treeService).ensureTreeExists(treeId);
//        verifyNoInteractions(citationService, relationshipManager);
//    }
//
//    @Test
//    void removeCitationFromEvent_shouldThrowExceptionWhenCitationDoesNotExist() {
//
//        doNothing().when(treeService).ensureTreeExists(treeId);
//        when(eventService.findEventById(eventId)).thenReturn(event);
//        when(citationService.findCitationById(citationId))
//                .thenThrow(new NodeNotFoundException("Citation not found"));
//
//        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> {
//            eventCitationManagementService.removeCitationFromEvent(treeId, eventId, citationId);
//        });
//        assertEquals("Citation not found", exception.getMessage());
//        verify(treeService).ensureTreeExists(treeId);
//        verify(eventService).findEventById(eventId);
//        verifyNoInteractions(relationshipManager);
//    }
//
//    @Test
//    void addCitationToEvent_shouldThrowExceptionWhenCitationIsAlreadyInTheEvent() {
//        Event mockEvent = mock(Event.class);
//        doNothing().when(treeService).ensureTreeExists(treeId);
//        when(eventService.findEventById(eventId)).thenReturn(mockEvent);
//        when(citationService.findCitationById(citationId)).thenReturn(citation);
//
//        when(mockEvent.isCitationAlreadyInEvent(citationId))
//                .thenThrow(new NodeAlreadyInNodeException("Citation " + citationId + " is already in the event " + eventId));
//
//        NodeAlreadyInNodeException exception = assertThrows(NodeAlreadyInNodeException.class, () -> {
//            eventCitationManagementService.addCitationToEvent(treeId, eventId, citationId);
//        });
//
//        assertEquals("Citation " + citationId + " is already in the event " + eventId, exception.getMessage());
//        verify(treeService).ensureTreeExists(treeId);
//        verify(eventService).findEventById(eventId);
//        verify(citationService).findCitationById(citationId);
//        verifyNoInteractions(relationshipManager);
//    }
//
//    @Test
//    void addCitationToEvent_shouldAddCitationWhenValidInputs() {
//        doNothing().when(treeService).ensureTreeExists(treeId);
//        when(eventService.findEventById(eventId)).thenReturn(event);
//        when(citationService.findCitationById(citationId)).thenReturn(citation);
//
//        eventCitationManagementService.addCitationToEvent(treeId, eventId, citationId);
//
//        verify(treeService).ensureTreeExists(treeId);
//        verify(eventService).findEventById(eventId);
//        verify(citationService).findCitationById(citationId);
//        verify(relationshipManager).addEventCitationRelationship(event, citation);
//    }
//}
