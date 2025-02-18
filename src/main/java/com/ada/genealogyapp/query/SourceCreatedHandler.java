package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_CREATED")
public class SourceCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        String sourceId = context.get("sourceId");
        log.info("Source with ID: " + sourceId + " created and added to tree with ID: " + treeId);
    }
}