package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.dto.FileFilterRequest;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class FileViewService {

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    private final FileRepository fileRepository;

    private final FileService fileService;

    public FileViewService(ObjectMapper objectMapper, TreeService treeService, FileRepository fileRepository, FileService fileService) {
        this.objectMapper = objectMapper;
        this.treeService = treeService;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    public Page<FileResponse> getFiles(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeService.ensureTreeExists(treeId);
        FileFilterRequest filterRequest = objectMapper.readValue(filter, FileFilterRequest.class);
        return fileRepository.findByTreeIdAndFilteredNameAndType(
                treeId,
                Optional.ofNullable(filterRequest.getName()).orElse(""),
                Optional.ofNullable(filterRequest.getType()).orElse(""),
                pageable
        );
    }

    public FileResponse getFile(UUID treeId, UUID fileId) {
        treeService.ensureTreeExists(treeId);
        fileService.ensureFileExists(fileId);
        return fileRepository.findByTreeIdAndFileId(treeId, fileId)
                .orElseThrow(() -> new NodeNotFoundException("File " + fileId.toString() + " not found for tree " + treeId.toString()));
    }
}
