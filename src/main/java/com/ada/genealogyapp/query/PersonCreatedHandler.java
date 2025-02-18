package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PERSON_CREATED")
public class PersonCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        String personId = context.get("personId");
        log.info("Person with ID: " + personId + " created and added to tree with ID: " + treeId);
    }
}