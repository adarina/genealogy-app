package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventServiceImpl eventService;

    UUID eventId;
    Event event;

    @BeforeEach
    void setUp() {

        eventId = UUID.randomUUID();
        event = new Event();
        event.setId(eventId);
    }

    @Test
    void findEventById_shouldReturnEventWhenFound() {

        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        Event result = eventService.findEventById(eventId);

        assertNotNull(result);
        assertEquals(eventId, result.getId());
        verify(eventRepository).findById(eventId);
    }

    @Test
    void findEventById_shouldThrowExceptionWhenNotFound() {

        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class,
                () -> eventService.findEventById(eventId));

        assertEquals("Event not found with ID: " + eventId, exception.getMessage());
        verify(eventRepository).findById(eventId);
    }

    @Test
    void ensureEventExists_shouldNotThrowWhenEventExists() {

        when(eventRepository.existsById(eventId)).thenReturn(true);

        assertDoesNotThrow(() -> eventService.ensureEventExists(eventId));
        verify(eventRepository).existsById(eventId);
    }

    @Test
    void ensureEventExists_shouldThrowExceptionWhenEventDoesNotExist() {

        when(eventRepository.existsById(eventId)).thenReturn(false);

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class,
                () -> eventService.ensureEventExists(eventId));

        assertEquals("Event not found with ID: " + eventId, exception.getMessage());
        verify(eventRepository).existsById(eventId);
    }

    @Test
    void saveEvent_shouldSaveEventAndLogMessage() {

        when(eventRepository.save(event)).thenReturn(event);

        eventService.saveEvent(event);

        verify(eventRepository).save(event);

    }

    @Test
    void deleteEvent_shouldDeleteEventAndLogMessage() {

        eventService.deleteEvent(event);

        verify(eventRepository).delete(event);
    }
}

