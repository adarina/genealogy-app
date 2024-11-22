package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.model.File;

import java.util.UUID;

public interface FileService {

    File findFileById(UUID fileId);

    void ensureFileExists(UUID fileId);

    void saveFile(File file);
}
