package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.FileJsonRequest;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.file.dto.params.CreateAndAddMultipartFileToCitationRequestParams;
import com.ada.genealogyapp.file.dto.params.CreateFileRequestParams;
import com.ada.genealogyapp.file.dto.params.CreateMultipartFileRequestParams;
import com.ada.genealogyapp.file.dto.params.SaveFileParams;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileCreationService {

    private final FileService fileService;

    private final FileStorageService fileStorageService;

    private final CitationService citationService;

    private final FileValidationService fileValidationService;

    public void validateAndSaveFile(BaseParams params, File file) {
        fileValidationService.validateFile(file);
        fileService.saveFile(SaveFileParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .fileId(file.getId())
                .file(file)
                .build());
    }

    @TransactionalInNeo4j
    public File createFile(CreateFileRequestParams params) {
        File file = File.builder()
                .id(UUID.randomUUID().toString())
                .filename(params.getFileRequest().getName())
                .name(params.getFileRequest().getName())
                .path(params.getFileRequest().getPath())
                .type(params.getFileRequest().getType())
                .build();
        validateAndSaveFile(params, file);
        return file;
    }

    @TransactionalInNeo4j
    public void createFile(CreateMultipartFileRequestParams params) {
        File file = fileStorageService.saveMultipartFileToFileSystem(params.getMultipartFile());
        validateAndSaveFile(params, file);
    }

    @TransactionalInNeo4j
    public void createAndAddFileToCitation(CreateAndAddMultipartFileToCitationRequestParams params) {
        File file = fileStorageService.saveMultipartFileToFileSystem(params.getMultipartFile());
        validateAndSaveFile(params, file);

        citationService.addFileToCitation(AddFileToCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(params.getCitationId())
                .fileId(file.getId())
                .build());
    }

    @TransactionalInNeo4j
    public Map<String, File> createFiles(String userId, String treeId, List<FileJsonRequest> fileRequests) {
        Map<String, File> createdFilesMap = new HashMap<>();
        List<Map<String, Object>> files = new ArrayList<>();

        for (FileJsonRequest request : fileRequests) {
            File file = File.builder()
                    .id(UUID.randomUUID().toString())
                    .path(request.getPath())
                    .type(request.getType())
                    .name(request.getName())
                    .build();
            fileValidationService.validateFile(file);

            Map<String, Object> fileData = new HashMap<>();
            fileData.put("id", file.getId());
            fileData.put("path", file.getPath());
            fileData.put("type", file.getType());
            fileData.put("name", file.getName());

            files.add(fileData);
            createdFilesMap.put(request.getId(), file);
        }
        fileService.saveFilesBatch(userId, treeId, files);
        return createdFilesMap;
    }
}
