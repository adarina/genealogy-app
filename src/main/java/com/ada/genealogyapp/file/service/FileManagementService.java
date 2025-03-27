package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileRequestParams;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileManagementService {

    private final FileService fileService;

    private final FileValidationService fileValidationService;

    @TransactionalInNeo4j
    public void updateFile(UpdateFileRequestParams params) {
        File file = File.builder()
                .name(params.getFileRequest().getName())
                .build();
        fileValidationService.validateFile(file);
        fileService.updateFile(UpdateFileParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .fileId(params.getFileId())
                .file(file)
                .build());
    }

    @TransactionalInNeo4j
    public void deleteFile(DeleteFileParams params) {
        fileService.deleteFile(params);
    }
}
