package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@Slf4j
public class FileViewService {

    private final FileSearchService fileSearchService;

    private final TreeService treeService;

    private final FileRepository fileRepository;

    private final FileService fileService;

    public FileViewService(FileSearchService fileSearchService, TreeService treeService, FileRepository fileRepository, FileService fileService) {
        this.fileSearchService = fileSearchService;
        this.treeService = treeService;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }


    public List<File> getFiles(UUID treeId) {
        return fileSearchService.getFilesByTreeIdOrThrowNodeNotFoundException(treeId);
    }

    public FileResponse getFile(UUID treeId, UUID fileId) {
        treeService.ensureTreeExists(treeId);
        fileService.ensureFileExists(fileId);
        return fileRepository.findByTreeIdAndFileId(treeId, fileId)
                .orElseThrow(() -> new NodeNotFoundException("File " + fileId.toString() + " not found for tree " + treeId.toString()));
    }
}
