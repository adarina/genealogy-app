package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("CITATION_UPDATED")
public class CitationUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        log.info("Citation with ID: " + citationId + " updated");
    }
}