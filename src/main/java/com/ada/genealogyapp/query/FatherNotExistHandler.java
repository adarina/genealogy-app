package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FATHER_NOT_EXIST")
public class FatherNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String fatherId = context.get("fatherId");
        throw new NodeNotFoundException("Father not exist with ID: " + fatherId);
    }
}