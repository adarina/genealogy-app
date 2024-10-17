package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

import java.util.UUID;

public interface EventService {
    void checkEventApplicable(Event event, String type);

    void saveEvent(Event event);

    Event findEventByIdOrThrowNodeNotFoundException(UUID eventId);

}
