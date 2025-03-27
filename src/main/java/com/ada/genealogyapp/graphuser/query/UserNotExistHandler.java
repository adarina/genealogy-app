package com.ada.genealogyapp.graphuser.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("USER_NOT_EXIST")
public class UserNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String userId = context.get(IdType.USER_ID);
        throw new NodeNotFoundException("User not exist with ID: " + userId);
    }
}