package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("EVENT_UPDATED")
public class EventUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String eventId = context.get(IdType.EVENT_ID);
        log.info("Event with ID: " + eventId + " updated");
    }
}