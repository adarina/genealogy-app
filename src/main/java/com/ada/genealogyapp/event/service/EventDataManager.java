package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.event.dto.params.*;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventDataManager implements EventService {

    private final EventRepository eventRepository;

    private final QueryResultProcessor processor;


    @TransactionalInNeo4j
    public void saveEvent(SaveEventParams params) {
        String result = eventRepository.save(params.getUserId(), params.getTreeId(), params.getEventId(), params.getEvent().getDescription(), params.getEvent().getPlace(), params.getEvent().getType().name(), params.getEvent().getDate());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.EVENT_ID, params.getEventId()));
    }

    @TransactionalInNeo4j
    public void deleteEvent(DeleteEventParams params) {
        String result = eventRepository.delete(params.getUserId(), params.getTreeId(), params.getEventId());
        processor.process(result, Map.of(IdType.EVENT_ID, params.getEventId()));
    }

    @TransactionalInNeo4j
    public void updateEvent(UpdateEventParams params) {
        String result = eventRepository.update(params.getUserId(), params.getTreeId(), params.getEventId(), params.getEvent().getDescription(), params.getEvent().getPlace(), params.getEvent().getDate(), params.getEvent().getType().name());
        processor.process(result, Map.of(IdType.EVENT_ID,  params.getEventId()));
    }

    @TransactionalInNeo4j
    public void updateEventWithParticipant(UpdateEventWithParticipantParams params) {
        String result = eventRepository.update(params.getUserId(), params.getTreeId(), params.getEventId(), params.getEvent().getDescription(), params.getEvent().getPlace(), params.getEvent().getDate(), params.getEvent().getType().name(), params.getParticipantId(), params.getRelationshipType());
        processor.process(result, Map.of(IdType.PARTICIPANT_ID, params.getParticipantId(), IdType.EVENT_ID, params.getEventId()));
    }

    @TransactionalInNeo4j
    public void addParticipantToEvent(AddParticipantToEventParams params) {
        String result = eventRepository.addParticipant(params.getUserId(), params.getTreeId(), params.getEventId(), params.getParticipantId(), params.getRelationshipType());
        processor.process(result, Map.of(IdType.EVENT_ID, params.getEventId(), IdType.PARTICIPANT_ID, params.getParticipantId()));
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(AddCitationToEventParams params) {
        String result = eventRepository.addCitation(params.getUserId(), params.getTreeId(), params.getEventId(), params.getCitationId());
        processor.process(result, Map.of(IdType.EVENT_ID, params.getUserId(), IdType.CITATION_ID, params.getCitationId()));
    }

    @TransactionalInNeo4j
    public void removeParticipantFromEvent(RemoveParticipantFromEventParams params) {
        String result = eventRepository.removeParticipant(params.getUserId(), params.getTreeId(), params.getEventId(), params.getParticipantId());
        processor.process(result, Map.of(IdType.EVENT_ID, params.getEventId(), IdType.PARTICIPANT_ID, params.getParticipantId()));
    }

    @TransactionalInNeo4j
    public void removeCitationFromEvent(RemoveCitationFromEventParams params) {
        String result = eventRepository.removeCitation(params.getUserId(), params.getTreeId(), params.getEventId(), params.getCitationId());
        processor.process(result, Map.of(IdType.EVENT_ID, params.getEventId(), IdType.CITATION_ID, params.getCitationId()));
    }
}
