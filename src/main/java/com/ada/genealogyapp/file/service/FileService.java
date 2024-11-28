package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.model.File;

public interface FileService {

    File findFileById(String fileId);

    void ensureFileExists(String fileId);

    void saveFile(File file);
}
