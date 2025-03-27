package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PARTICIPANT_REMOVED_FROM_EVENT")
public class ParticipantRemovedFromEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String eventId = context.get(IdType.EVENT_ID);
        String participantId = context.get(IdType.PARTICIPANT_ID);
        log.info("Participant with ID: " + participantId + " removed from event with ID: " + eventId);
    }
}