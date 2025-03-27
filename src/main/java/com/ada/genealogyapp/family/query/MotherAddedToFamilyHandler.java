package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("MOTHER_ADDED_TO_FAMILY")
public class MotherAddedToFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        String personId = context.get(IdType.PERSON_ID);
        log.info("Person with ID: " + personId + " added as mother to family with ID: " + familyId);
    }
}