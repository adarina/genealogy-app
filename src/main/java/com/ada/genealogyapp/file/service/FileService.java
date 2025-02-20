package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.NonNull;

public interface FileService {



    void ensureFileExists(String fileId);

    void saveFile(String userID, String treeId, File file);
}
