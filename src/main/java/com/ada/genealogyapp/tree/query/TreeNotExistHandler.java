package com.ada.genealogyapp.tree.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("TREE_NOT_EXIST")
public class TreeNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        throw new NodeNotFoundException("Tree not exist with ID: " + treeId);
    }
}
