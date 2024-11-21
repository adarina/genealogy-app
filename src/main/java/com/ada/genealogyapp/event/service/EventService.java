package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

import java.util.UUID;

public interface EventService {
    Event findEventById(UUID eventId);
    void saveEvent(Event event);
    void ensureEventExists(UUID eventId);
    void deleteEvent(Event event);
}
