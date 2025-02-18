package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FAMILY_CREATED")
public class FamilyCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        String familyId = context.get("familyId");
        log.info("Family with ID: " + familyId + " created and added to tree with ID: " + treeId);
    }
}