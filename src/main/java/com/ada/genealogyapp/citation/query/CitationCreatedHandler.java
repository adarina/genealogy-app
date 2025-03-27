package com.ada.genealogyapp.citation.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_CREATED")
public class CitationCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String citationId = context.get(IdType.CITATION_ID);
        log.info("Citation with ID: " + citationId + " created and added to tree with ID: " + treeId);
    }
}