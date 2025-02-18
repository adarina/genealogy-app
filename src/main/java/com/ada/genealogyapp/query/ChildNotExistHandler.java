package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("CHILD_NOT_EXIST")
public class ChildNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String childId = context.get("childId");
        throw new NodeNotFoundException("Child not exist with ID: " + childId);
    }
}