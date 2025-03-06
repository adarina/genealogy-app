package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.tree.model.Tree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EventCreationServiceTest {


    @Mock
    private EventService eventService;

    @Mock
    private EventValidationService eventValidationService;

    @InjectMocks
    private EventCreationService eventCreationService;

    @Test
    void createEvent_shouldValidateAndSaveEvent() {
        String userId = "user123";
        String treeId = "tree123";
        EventRequest eventRequest = new EventRequest();
        eventRequest.setDescription("event123");
        eventRequest.setType(EventType.BIRTH);
        eventRequest.setDate("1800-09-12");
        eventRequest.setPlace("place123");


        Event createdEvent = eventCreationService.createEvent(userId, treeId, eventRequest);

        assertNotNull(createdEvent);
        assertEquals(EventType.BIRTH, createdEvent.getType());
        assertEquals("event123", createdEvent.getDescription());
        assertEquals("place123", createdEvent.getPlace());
        assertEquals("1800-09-12", createdEvent.getDate());

        verify(eventValidationService).validateEvent(createdEvent);
        verify(eventService).saveEvent(userId, treeId, createdEvent);
    }

    @Test
    void createFamily_withTreeAndTypeAndPlaceAndDateAndDescription_shouldValidateAndSaveEvent() {
        String userId = "user123";
        Tree tree = Tree.builder().id("tree123").build();
        EventType type = EventType.BIRTH;
        String description = "event123";
        String date = "1800-09-12";
        String place = "place123";


        Event createdEvent = eventCreationService.createEvent(userId, tree, type, place, description, date);

        assertNotNull(createdEvent);
        assertEquals(EventType.BIRTH, createdEvent.getType());
        assertEquals("event123", createdEvent.getDescription());
        assertEquals("place123", createdEvent.getPlace());
        assertEquals("1800-09-12", createdEvent.getDate());

        verify(eventValidationService).validateEvent(createdEvent);
        verify(eventService).saveEvent(userId, tree.getId(), createdEvent);
    }
}
