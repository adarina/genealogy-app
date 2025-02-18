package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PARTICIPANT_NOT_EXIST")
public class ParticipantNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String participantId = context.get("participantId");
        throw new NodeNotFoundException("Participant not exist with ID: " + participantId);
    }
}