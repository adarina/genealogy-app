package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.event.dto.params.DeleteEventParams;
import com.ada.genealogyapp.event.dto.params.SaveEventParams;
import com.ada.genealogyapp.event.dto.params.UpdateEventParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventDataManagerTest {

    @Mock
    EventRepository eventRepository;

    @Mock
    QueryResultProcessor processor;

    @InjectMocks
    EventDataManager eventDataManager;
    
    
    @Test
    void saveEvent_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        SaveEventParams params = SaveEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        String result = "EVENT_CREATED";
        when(eventRepository.save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate())).thenReturn(result);

        eventDataManager.saveEvent(params);

        verify(eventRepository).save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());
        verify(processor).process(result, Map.of(IdType.TREE_ID, treeId, IdType.EVENT_ID, eventId));
    }

    @Test
    void saveEvent_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        SaveEventParams params = SaveEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(eventRepository).save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.saveEvent(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());
    }

    @Test
    void saveEvent_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        SaveEventParams params = SaveEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(eventRepository).save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.saveEvent(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).save(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());
    }

    @Test
    void updateEvent_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        UpdateEventParams params = UpdateEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        String result = "EVENT_UPDATED";
        when(eventRepository.update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name())).thenReturn(result);

        eventDataManager.updateEvent(params);

        verify(eventRepository).update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());
        verify(processor).process(result, Map.of(IdType.EVENT_ID, eventId));
    }

    @Test
    void updateEvent_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        UpdateEventParams params = UpdateEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(eventRepository).update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.updateEvent(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());
    }

    @Test
    void updateEvent_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;
        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        UpdateEventParams params = UpdateEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .event(event)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(eventRepository).update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.updateEvent(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).update(userId, treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());
    }

    @Test
    void deleteEvent_shouldInvokeRepositoryAndProcessor() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        DeleteEventParams params = DeleteEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        String result = "EVENT_DELETED";
        when(eventRepository.delete(userId, treeId, eventId)).thenReturn(result);

        eventDataManager.deleteEvent(params);

        verify(eventRepository).delete(userId, treeId, eventId);
        verify(processor).process(result, Map.of(IdType.EVENT_ID, eventId));
    }

    @Test
    void deleteEvent_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        DeleteEventParams params = DeleteEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + userId))
                .when(eventRepository).delete(userId, treeId, eventId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.deleteEvent(params));

        assertEquals("Tree not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).delete(userId, treeId, eventId);
    }

    @Test
    void deleteEvent_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        DeleteEventParams params = DeleteEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(eventRepository).delete(userId, treeId, eventId);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventDataManager.deleteEvent(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(eventRepository).delete(userId, treeId, eventId);
    }
}

