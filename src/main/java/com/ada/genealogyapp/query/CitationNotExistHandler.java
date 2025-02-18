package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CITATION_NOT_EXIST")
public class CitationNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String citationId = context.get("citationId");
        throw new NodeNotFoundException("Citation not exist with ID: " + citationId);
    }
}