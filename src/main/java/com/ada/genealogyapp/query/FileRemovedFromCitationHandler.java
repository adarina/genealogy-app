package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FILE_REMOVED_FROM_CITATION")
public class FileRemovedFromCitationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        String fileId = context.get("fileId");
        log.info("File with ID: " + fileId + " removed from citation with ID: " + citationId);
    }
}