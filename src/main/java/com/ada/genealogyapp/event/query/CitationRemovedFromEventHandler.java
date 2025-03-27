package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("CITATION_REMOVED_FROM_EVENT")
public class CitationRemovedFromEventHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String eventId = context.get(IdType.EVENT_ID);
        String citationId = context.get(IdType.CITATION_ID);
        log.info("Citation with ID: " + citationId + " removed from event with ID: " + eventId);
    }
}