package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("TREE_NOT_EXIST")
public class TreeNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        throw new NodeNotFoundException("Tree not exist with ID: " + treeId);
    }
}
