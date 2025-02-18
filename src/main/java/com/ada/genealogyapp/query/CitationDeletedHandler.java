package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_DELETED")
public class CitationDeletedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        log.info("Citation with ID: " + citationId + " deleted");
    }
}