package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.EventTypeApplicableException;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
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

    public void checkEventApplicable(Event event, String type) {
        if (!event.getEventType().getApplicableTo().equals(type)) {
            throw new EventTypeApplicableException("This event type is not applicable to a {}: " + type);
        }
    }

    public void saveEvent(Event event) {
        Event savedEvent = eventRepository.save(event);
        log.info("Event saved successfully: {}", savedEvent);
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
}
