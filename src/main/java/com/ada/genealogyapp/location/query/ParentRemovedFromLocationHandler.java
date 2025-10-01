package com.ada.genealogyapp.location.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("PARENT_REMOVED_FROM_LOCATION")
public class ParentRemovedFromLocationHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String locationId = context.get(IdType.LOCATION_ID);
        String parentId = context.get(IdType.PARENT_ID);
        log.info("Parent with ID: " + parentId + " removed from location with ID: " + locationId);
    }
}
