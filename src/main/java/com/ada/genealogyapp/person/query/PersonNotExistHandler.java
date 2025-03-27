package com.ada.genealogyapp.person.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("PERSON_NOT_EXIST")
public class PersonNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String personId = context.get(IdType.PERSON_ID);
        throw new NodeNotFoundException("Person not exist with ID: " + personId);
    }
}

