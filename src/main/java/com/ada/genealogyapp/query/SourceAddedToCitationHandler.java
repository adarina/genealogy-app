package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_ADDED_TO_CITATION")
public class SourceAddedToCitationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        String sourceId = context.get("sourceId");
        log.info("Source with ID: " + sourceId + " added to citation with ID: " + citationId);
    }
}