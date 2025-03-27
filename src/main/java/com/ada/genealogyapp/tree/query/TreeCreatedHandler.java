package com.ada.genealogyapp.tree.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("TREE_CREATED")
public class TreeCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String userId = context.get(IdType.USER_ID);
        log.info("Tree with ID: " + treeId + " created and added to user with ID: " + userId);
    }
}