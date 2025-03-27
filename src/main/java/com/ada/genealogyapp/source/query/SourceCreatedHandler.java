package com.ada.genealogyapp.source.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("SOURCE_CREATED")
public class SourceCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String sourceId = context.get(IdType.SOURCE_ID);
        log.info("Source with ID: " + sourceId + " created and added to tree with ID: " + treeId);
    }
}