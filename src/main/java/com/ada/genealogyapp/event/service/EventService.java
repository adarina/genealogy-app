package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.model.Event;

public interface EventService {
    Event findEventById(String eventId);
    void saveEvent(String userId, String treeId, Event event);
    void saveEventWithParticipant(String userId, String treeId, Event event, String participantId, String relationshipType);
    void ensureEventExists(String eventId);
    void deleteEvent(String userId, String treeId, String eventId);
    void updateEvent(String userId, String treeId, String eventId, Event event);
    void updateEventWithParticipant(String userId, String treeId, String eventId, Event event, String participantId, String relationshipType);
    void addParticipantToEvent(String userId, String treeId, String eventId, String participantId, String relationshipType);
    void removeParticipantFromEvent(String userId, String treeId, String eventId, String participantId);
    void removeCitationFromEvent(String userId, String treeId, String eventId, String citationId);
    void addCitationToEvent(String userId, String treeId, String eventId, String citationId);

}
