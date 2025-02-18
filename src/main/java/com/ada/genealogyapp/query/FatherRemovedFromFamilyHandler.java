package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FATHER_REMOVED_FROM_FAMILY")
public class FatherRemovedFromFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String familyId = context.get("familyId");
        String fatherId = context.get("fatherId");
        log.info("Father with ID: " + fatherId + " removed from family with ID: " + familyId);
    }
}