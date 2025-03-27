package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FATHER_REMOVED_FROM_FAMILY")
public class FatherRemovedFromFamilyHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        String fatherId = context.get(IdType.FATHER_ID);
        log.info("Father with ID: " + fatherId + " removed from family with ID: " + familyId);
    }
}