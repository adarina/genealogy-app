package com.ada.genealogyapp.location.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("LOCATION_NOT_EXIST")
public class LocationNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String locationId = context.get(IdType.LOCATION_ID);
        throw new NodeNotFoundException("Location not exist with ID: " + locationId);
    }
}
