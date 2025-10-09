package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.SaveFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileParams;

import java.util.List;
import java.util.Map;


public interface FileService {
    void saveFile(SaveFileParams params);

    void updateFile(UpdateFileParams params);

    void deleteFile(DeleteFileParams params);

    void saveFilesBatch(String userId, String treeId, List<Map<String, Object>> filesData);
}
