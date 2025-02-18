package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_NOT_EXIST")
public class SourceNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String sourceId = context.get("sourceId");
        throw new NodeNotFoundException("Source not exist with ID: " + sourceId);
    }
}