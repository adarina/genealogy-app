package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.relationship.EventParticipant;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.participant.service.ParticipantService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class EventManagementService {

    private final TreeService treeService;

    private final EventService eventService;

    private final EventValidationService eventValidationService;

    private final ParticipantService participantService;

    @TransactionalInNeo4j
    public void updateEvent(String treeId, String eventId, @NonNull EventRequest eventRequest) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);

        event.setDescription(eventRequest.getDescription());
        event.setPlace(eventRequest.getPlace());
        event.setDate(eventRequest.getDate());
        event.setType(eventRequest.getType());

        eventValidationService.validateEvent(event);
        eventService.saveEvent(event);
        log.info("Event updated: {}", event);
    }

    @TransactionalInNeo4j
    public void updateEvent(String treeId, String eventId, @NonNull ParticipantEventRequest eventRequest, String participantId) {
        treeService.ensureTreeExists(treeId);
        Participant participant = participantService.findParticipantById(participantId);
        Event event = eventService.findEventById(eventId);

        event.getParticipants().stream()
                .filter(rel -> rel.getParticipant().getId().equals(participantId))
                .findFirst().ifPresent(existingRel -> event.getParticipants().remove(existingRel));

        EventParticipant relationship = EventParticipant.builder()
                .participant(participant)
                .relationship(eventRequest.getRelationship())
                .build();

        event.setDescription(eventRequest.getDescription());
        event.setPlace(eventRequest.getPlace());
        event.setDate(eventRequest.getDate());
        event.setType(eventRequest.getType());
        event.getParticipants().add(relationship);

        eventValidationService.validateEvent(event);
        eventService.saveEvent(event);
        log.info("Event with participant {} updated: {}", participantId, event);
    }

    @TransactionalInNeo4j
    public void removeParticipantFromEvent(String treeId, String participantId, String eventId) {
        treeService.ensureTreeExists(treeId);
        participantService.ensureParticipantExists(participantId);
        Event event = eventService.findEventById(eventId);

        event.getParticipants().stream()
                .filter(rel -> rel.getParticipant().getId().equals(participantId))
                .findFirst().ifPresent(existingRel -> event.getParticipants().remove(existingRel));

        eventService.saveEvent(event);
        log.info("Event {} removed from participant {}", eventId, participantId);
    }

    @TransactionalInNeo4j
    public void deleteEvent(String treeId, String eventId) {
        treeService.ensureTreeExists(treeId);
        Event event = eventService.findEventById(eventId);

        eventService.deleteEvent(event);
    }
}
