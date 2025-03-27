package com.ada.genealogyapp.file.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component("FILE_NOT_EXIST")
public class FileNotExistHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String fileId = context.get(IdType.FILE_ID);
        throw new NodeNotFoundException("File not exist with ID: " + fileId);
    }
}