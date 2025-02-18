package com.ada.genealogyapp.file.mapper.export.json;

import com.ada.genealogyapp.file.dto.FileJsonRequest;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.export.json.JsonMapper;
import org.springframework.stereotype.Component;

@Component
public class FileJsonMapper implements JsonMapper<File, FileJsonRequest> {

    @Override
    public FileJsonRequest map(File file) {
        return FileJsonRequest.builder()
                .id(file.getId())
                .name(file.getName())
                .filename(file.getFilename())
                .type(file.getType())
                .path(file.getPath())
                .build();
    }

    @Override
    public Class<File> getEntityType() {
        return File.class;
    }
}