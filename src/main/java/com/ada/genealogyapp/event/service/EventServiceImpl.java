package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NodeNotFoundException("Event not found with ID: " + eventId));
    }

    public void ensureEventExists(UUID eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NodeNotFoundException("Event not found with ID: " + eventId);
        }
    }

    @TransactionalInNeo4j
    public void saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Event saved successfully: {}", savedEvent);
    }

    @TransactionalInNeo4j
    public void deleteEvent(Event event) {
        eventRepository.delete(event);
        log.info("Event deleted successfully: {}", event.getId());
    }
}
