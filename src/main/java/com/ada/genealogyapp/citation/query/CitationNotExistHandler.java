package com.ada.genealogyapp.citation.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_NOT_EXIST")
public class CitationNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String citationId = context.get(IdType.CITATION_ID);
        throw new NodeNotFoundException("Citation not exist with ID: " + citationId);
    }
}