package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PERSON_NOT_EXIST")
public class PersonNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String personId = context.get("personId");
        throw new NodeNotFoundException("Person not exist with ID: " + personId);
    }
}

