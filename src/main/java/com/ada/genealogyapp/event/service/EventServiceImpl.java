package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEventByIdOrThrowNodeNotFoundException(UUID eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            log.info("Event found: {}", event.get());
        } else {
            log.error("No event found with id: {}", eventId);
            throw new NodeNotFoundException("No event found with id: " + eventId);
        }
        return event.get();
    }

    public Event findEventById(UUID eventId) {
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isPresent()) {
            log.info("Event found: {}", event.get());
            return event.get();
        } else {
            log.error("No event found with id: {}", eventId);
            return null;
        }
    }
}
