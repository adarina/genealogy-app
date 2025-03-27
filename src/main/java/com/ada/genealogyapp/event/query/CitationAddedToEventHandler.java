package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_ADDED_TO_EVENT")
public class CitationAddedToEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String eventId = context.get(IdType.EVENT_ID);
        String citationId = context.get(IdType.CITATION_ID);
        log.info("Citation with ID: " + citationId + " added to event with ID: " + eventId);
    }
}