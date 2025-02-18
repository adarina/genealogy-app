package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("MOTHER_ADDED_TO_FAMILY")
public class MotherAddedToFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String familyId = context.get("familyId");
        String personId = context.get("personId");
        log.info("Person with ID: " + personId + " added as mother to family with ID: " + familyId);
    }
}