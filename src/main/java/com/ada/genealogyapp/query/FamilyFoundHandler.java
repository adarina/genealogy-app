package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FAMILY_FOUND")
public class FamilyFoundHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {

    }
}
