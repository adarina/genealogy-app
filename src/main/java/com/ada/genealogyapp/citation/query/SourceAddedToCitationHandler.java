package com.ada.genealogyapp.citation.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_ADDED_TO_CITATION")
public class SourceAddedToCitationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String citationId = context.get(IdType.CITATION_ID);
        String sourceId = context.get(IdType.SOURCE_ID);
        log.info("Source with ID: " + sourceId + " added to citation with ID: " + citationId);
    }
}