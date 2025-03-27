package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FAMILY_UPDATED")
public class FamilyUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        log.info("Family with ID: " + familyId + " updated");
    }
}