package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

import java.util.UUID;

public interface EventService {

    Event findEventByIdOrThrowNodeNotFoundException(UUID eventId);

    Event findEventById(UUID eventId);

}
