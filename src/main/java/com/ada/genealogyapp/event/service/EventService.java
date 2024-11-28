package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

public interface EventService {
    Event findEventById(String eventId);
    void saveEvent(Event event);
    void ensureEventExists(String eventId);
    void deleteEvent(Event event);
}
