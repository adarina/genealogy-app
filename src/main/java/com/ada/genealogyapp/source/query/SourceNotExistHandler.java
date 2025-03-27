package com.ada.genealogyapp.source.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_NOT_EXIST")
public class SourceNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String sourceId = context.get(IdType.SOURCE_ID);
        throw new NodeNotFoundException("Source not exist with ID: " + sourceId);
    }
}