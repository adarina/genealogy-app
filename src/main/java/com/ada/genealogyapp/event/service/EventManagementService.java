package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.tree.service.TreeService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class EventManagementService {

    private final TreeTransactionService treeTransactionService;

    private final TreeService treeService;

    private final EventService eventService;

    private final CitationService citationService;

    public EventManagementService(TreeTransactionService treeTransactionService, TreeService treeService, EventService eventService, CitationService citationService) {
        this.treeTransactionService = treeTransactionService;
        this.treeService = treeService;
        this.eventService = eventService;
        this.citationService = citationService;
    }

    public Event validateTreeAndEvent(UUID treeId, UUID eventId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
    }

    public Citation validateTreeEventAndCitation(UUID treeId, UUID eventId, UUID citationId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
        return citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);
    }

    @TransactionalInNeo4j
    public void updateEventData(UUID treeId, UUID eventId, EventRequest eventRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        validateTreeAndEvent(treeId, eventId);

        updateEventType(tx, eventId, eventRequest.getType());
        updateDate(tx, eventId, eventRequest.getDate());
        updatePlace(tx, eventId, eventRequest.getPlace());
        updateDescription(tx, eventId, eventRequest.getDescription());

        log.info("Event updated successfully: {}", eventId);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void updateEventType(Transaction tx, UUID eventId, EventType eventType) {
        if (nonNull(eventType)) {
            String cypher = "MATCH (e:Event {id: $eventId}) SET e.eventType = $eventType";
            tx.run(cypher, Map.of("eventId", eventId.toString(), "eventType", eventType.toString()));
        }
    }

    @TransactionalInNeo4j
    public void updateDate(Transaction tx, UUID eventId, LocalDate date) {
        if (nonNull(date)) {
            String cypher = "MATCH (e:Event {id: $eventId}) SET e.date = $date";
            tx.run(cypher, Map.of("eventId", eventId.toString(), "date", date));
        }
    }

    @TransactionalInNeo4j
    public void updatePlace(Transaction tx, UUID eventId, String place) {
        if (nonNull(place)) {
            String cypher = "MATCH (e:Event {id: $eventId}) SET e.place = $place";
            tx.run(cypher, Map.of("eventId", eventId.toString(), "place", place));
        }
    }

    @TransactionalInNeo4j
    public void updateDescription(Transaction tx, UUID eventId, String description) {
        if (nonNull(description)) {
            String cypher = "MATCH (e:Event {id: $eventId}) SET e.description = $description";
            tx.run(cypher, Map.of("eventId", eventId.toString(), "description", description));
        }
    }
}
