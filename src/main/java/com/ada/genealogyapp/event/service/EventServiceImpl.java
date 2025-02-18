package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.EventSaveResponse;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.relationship.EventParticipant;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    private final QueryResultProcessor queryResultProcessor;

    public EventServiceImpl(EventRepository eventRepository, QueryResultProcessor queryResultProcessor) {
        this.eventRepository = eventRepository;
        this.queryResultProcessor = queryResultProcessor;
    }

    public Event findEventById(String eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NodeNotFoundException("Event not found with ID: " + eventId));
    }

    public void ensureEventExists(String eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new NodeNotFoundException("Event not found with ID: " + eventId);
        }
    }

    @TransactionalInNeo4j
    public void saveEvent(String treeId, Event event) {
        String result = eventRepository.save(treeId, event.getId(), event.getDescription(), event.getPlace(), event.getType().name(), event.getDate());
        queryResultProcessor.process(result, Map.of("treeId", treeId, "eventId", event.getId()));
    }

    @TransactionalInNeo4j
    public void deleteEvent(String treeId, String eventId) {
        String result = eventRepository.delete(treeId, eventId);
        queryResultProcessor.process(result, Map.of("eventId", eventId));
    }

    @TransactionalInNeo4j
    public void updateEvent(String treeId, String eventId, Event event) {
        String result = eventRepository.update(treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name());
        queryResultProcessor.process(result, Map.of("eventId", eventId));
    }

    @TransactionalInNeo4j
    public void updateEventWithParticipant(String treeId, String eventId, Event event, String participantId, String relationshipType) {
        String result = eventRepository.update(treeId, eventId, event.getDescription(), event.getPlace(), event.getDate(), event.getType().name(), participantId, relationshipType);
        queryResultProcessor.process(result, Map.of("participantId", participantId, "eventId", eventId));
    }

    @TransactionalInNeo4j
    public void saveEventWithParticipant(String treeId, @NonNull Event event, String participantId, String relationshipType) {
        String result = eventRepository.save(treeId, event.getId(), event.getDescription(), event.getPlace(), event.getType().name(), event.getDate(), participantId, relationshipType);
        queryResultProcessor.process(result, Map.of("treeId", treeId, "participantId", participantId, "eventId", event.getId()));
    }

    @TransactionalInNeo4j
    public void addParticipantToEvent(String treeId, String eventId, String participantId, String relationshipType) {
        String result = eventRepository.addParticipant(treeId, eventId, participantId, relationshipType);
        queryResultProcessor.process(result, Map.of("eventId", eventId, "participantId", participantId));
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(String treeId, String eventId, String citationId) {
        String result = eventRepository.addCitation(treeId, eventId, citationId);
        queryResultProcessor.process(result, Map.of("eventId", eventId, "citationId", citationId));
    }

    @TransactionalInNeo4j
    public void removeParticipantFromEvent(String treeId, String eventId, String participantId) {
        String result = eventRepository.removeParticipant(treeId, eventId, participantId);
        queryResultProcessor.process(result, Map.of("eventId", eventId, "participantId", participantId));
    }

    @TransactionalInNeo4j
    public void removeCitationFromEvent(String treeId, String eventId, String citationId) {
        String result = eventRepository.removeCitation(treeId, eventId, citationId);
        queryResultProcessor.process(result, Map.of("eventId", eventId, "citationId", citationId));
    }
}
