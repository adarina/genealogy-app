package com.ada.genealogyapp.location.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("PARENT_NOT_EXIST")
public class ParentNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String parentId = context.get(IdType.PARENT_ID);
        throw new NodeNotFoundException("Parent not exist with ID: " + parentId);
    }
}
