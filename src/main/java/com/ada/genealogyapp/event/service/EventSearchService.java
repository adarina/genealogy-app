package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class EventSearchService {

    private final EventRepository eventRepository;

    public EventSearchService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Set<Event> findEventsByIds(Set<UUID> eventIds) {
        Set<Event> events = new HashSet<>();
        for (UUID eventId : eventIds) {
            Optional<Event> event = eventRepository.findById(eventId);
            if (event.isPresent()) {
                events.add(event.get());
            } else {
                log.error("No event found with id: {}", eventId);
            }
        }
        return events;
    }

    public List<Event> getEventsByTreeIdOrThrowNodeNotFoundException(UUID treeId) {
        List<Event> events = eventRepository.findAllByTreeId(treeId);
        if (!events.isEmpty()) {
            log.info("Events found for treeId {}: {}", treeId, events);
        } else {
            log.error("No events found for treeId: {}", treeId);
            throw new NodeNotFoundException("No events found for treeId: " + treeId);
        }
        return events;
    }
}
