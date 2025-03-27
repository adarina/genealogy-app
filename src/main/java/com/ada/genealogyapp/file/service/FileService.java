package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.SaveFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileParams;


public interface FileService {
    void saveFile(SaveFileParams params);

    void updateFile(UpdateFileParams params);

    void deleteFile(DeleteFileParams params);
}
