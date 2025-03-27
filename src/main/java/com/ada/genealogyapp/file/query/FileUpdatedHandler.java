package com.ada.genealogyapp.file.query;

import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Slf4j
@Component("FILE_UPDATED")
public class FileUpdatedHandler implements QueryResultHandler {
    @Override
    public void handleResult(Map<IdType, String> context) {
        String fileId = context.get(IdType.FILE_ID);
        log.info("File with ID: " + fileId + " updated");
    }
}