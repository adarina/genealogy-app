package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

public interface EventService {
    Event findEventById(String eventId);
    void saveEvent(String treeId, Event event);
    void saveEventWithParticipant(String treeId, Event event, String participantId, String relationshipType);
    void ensureEventExists(String eventId);
    void deleteEvent(String treeId, String eventId);
    void updateEvent(String treeId, String eventId, Event event);
    void updateEventWithParticipant(String treeId, String eventId, Event event, String participantId, String relationshipType);
    void addParticipantToEvent(String treeId, String eventId, String participantId, String relationshipType);
    void removeParticipantFromEvent(String treeId, String eventId, String participantId);
    void removeCitationFromEvent(String treeId, String eventId, String citationId);
    void addCitationToEvent(String treeId, String eventId, String citationId);

}
