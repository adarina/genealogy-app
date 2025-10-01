package com.ada.genealogyapp.location.query;

import com.ada.genealogyapp.query.QueryResultHandler;
import com.ada.genealogyapp.query.IdType;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("LOCATION_CREATED")
public class LocationCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String locationId = context.get(IdType.LOCATION_ID);
        log.info("Location with ID: " + locationId + " created and added to tree with ID: " + treeId);
    }
}