package com.ada.genealogyapp.event.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("EVENT_CREATED")
public class EventCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String eventId = context.get(IdType.EVENT_ID);
        log.info("Event with ID: " + eventId + " created and added to tree with ID: " + treeId);
    }
}