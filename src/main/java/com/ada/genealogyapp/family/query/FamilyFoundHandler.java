package com.ada.genealogyapp.family.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FAMILY_FOUND")
public class FamilyFoundHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {

    }
}
