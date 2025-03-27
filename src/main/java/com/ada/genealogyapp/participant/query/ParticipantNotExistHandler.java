package com.ada.genealogyapp.participant.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PARTICIPANT_NOT_EXIST")
public class ParticipantNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String participantId = context.get(IdType.PARTICIPANT_ID);
        throw new NodeNotFoundException("Participant not exist with ID: " + participantId);
    }
}