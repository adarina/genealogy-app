package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class EventSearchService {

    private final EventRepository eventRepository;

    public EventSearchService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Event findEventById(UUID eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NodeNotFoundException("No event found with ID: " + eventId));
    }
}
