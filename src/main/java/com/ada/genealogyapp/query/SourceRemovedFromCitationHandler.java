package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_REMOVED_FROM_CITATION")
public class SourceRemovedFromCitationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        String sourceId = context.get("sourceId");
        log.info("Source with ID: " + sourceId + " removed from citation with ID: " + citationId);
    }
}