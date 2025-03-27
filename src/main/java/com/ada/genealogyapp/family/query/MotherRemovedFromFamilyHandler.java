package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("MOTHER_REMOVED_FROM_FAMILY")
public class MotherRemovedFromFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        String motherId = context.get(IdType.MOTHER_ID);
        log.info("Mother with ID: " + motherId + " removed from family with ID: " + familyId);
    }
}