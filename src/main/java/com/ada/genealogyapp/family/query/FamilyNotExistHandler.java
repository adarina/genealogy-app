package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FAMILY_NOT_EXIST")
public class FamilyNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String familyId = context.get(IdType.FAMILY_ID);
        throw new NodeNotFoundException("Family not exist with ID: " + familyId);
    }
}
