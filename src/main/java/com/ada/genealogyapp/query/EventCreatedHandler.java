package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("EVENT_CREATED")
public class EventCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        String eventId = context.get("eventId");
        log.info("Event with ID: " + eventId + " created and added to tree with ID: " + treeId);
    }
}