package com.ada.genealogyapp.file.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FILE_CREATED")
public class FileCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String treeId = context.get(IdType.TREE_ID);
        String fileId = context.get(IdType.FILE_ID);
        log.info("File with ID: " + fileId + " created and added to tree with ID: " + treeId);
    }
}