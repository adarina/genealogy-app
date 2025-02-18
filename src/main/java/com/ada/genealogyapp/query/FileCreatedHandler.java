package com.ada.genealogyapp.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FILE_CREATED")
public class FileCreatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String treeId = context.get("treeId");
        String fileId = context.get("fileId");
        log.info("File with ID: " + fileId + " created and added to tree with ID: " + treeId);
    }
}