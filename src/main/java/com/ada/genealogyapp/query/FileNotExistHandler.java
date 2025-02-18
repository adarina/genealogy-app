package com.ada.genealogyapp.query;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FILE_NOT_EXIST")
public class FileNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<String, String> context) {
        String fileId = context.get("fileId");
        throw new NodeNotFoundException("File not exist with ID: " + fileId);
    }
}