package com.ada.genealogyapp.citation.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_REMOVED_FROM_CITATION")
public class SourceRemovedFromCitationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String citationId = context.get(IdType.CITATION_ID);
        String sourceId = context.get(IdType.SOURCE_ID);
        log.info("Source with ID: " + sourceId + " removed from citation with ID: " + citationId);
    }
}