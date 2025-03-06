package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("USER_NOT_EXIST")
public class UserNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String userId = context.get("userId");
        throw new NodeNotFoundException("User not exist with ID: " + userId);
    }
}