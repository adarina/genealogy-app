package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CHILD_REMOVED_FROM_FAMILY")
public class ChildRemovedFromFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String familyId = context.get("familyId");
        String childId = context.get("childId");
        log.info("Child with ID: " + childId + " removed from family with ID: " + familyId);
    }
}