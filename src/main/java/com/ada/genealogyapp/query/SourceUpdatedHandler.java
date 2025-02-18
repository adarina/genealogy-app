package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_UPDATED")
public class SourceUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String sourceId = context.get("sourceId");
        log.info("Source with ID: " + sourceId + " updated");
    }
}