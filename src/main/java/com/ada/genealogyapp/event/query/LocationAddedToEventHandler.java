package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("LOCATION_ADDED_TO_EVENT")
public class LocationAddedToEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String eventId = context.get(IdType.EVENT_ID);
        String locationId = context.get(IdType.LOCATION_ID);
        log.info("Location with ID: {} added to event with ID: {}", locationId, eventId);
    }
}
