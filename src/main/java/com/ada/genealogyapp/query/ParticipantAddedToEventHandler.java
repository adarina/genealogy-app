package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PARTICIPANT_ADDED_TO_EVENT")
public class ParticipantAddedToEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String eventId = context.get("eventId");
        String participantId = context.get("participantId");
        log.info("Participant with ID: " + participantId + " added to event with ID: " + eventId);
    }
}