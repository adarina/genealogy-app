package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_ADDED_TO_EVENT")
public class CitationAddedToEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String eventId = context.get("eventId");
        String citationId = context.get("citationId");
        log.info("Citation with ID: " + citationId + " added to event with ID: " + eventId);
    }
}