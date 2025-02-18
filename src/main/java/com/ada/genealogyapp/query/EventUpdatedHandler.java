package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("EVENT_UPDATED")
public class EventUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String eventId = context.get("eventId");
        log.info("Event with ID: " + eventId + " updated");
    }
}