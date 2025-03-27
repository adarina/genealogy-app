package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CHILD_ADDED_TO_FAMILY")
public class ChildAddedToFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        String personId = context.get(IdType.PERSON_ID);
        log.info("Person with ID: " + personId + " added as child to family with ID: " + familyId);
    }
}