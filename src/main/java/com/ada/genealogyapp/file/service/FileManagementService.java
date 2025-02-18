package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FileManagementService {

    private final TreeService treeService;

    private final FileService fileService;

    private final FileRepository fileRepository;

    public FileManagementService(TreeService treeService, FileService fileService, FileRepository fileRepository) {
        this.treeService = treeService;
        this.fileService = fileService;
        this.fileRepository = fileRepository;
    }



    @TransactionalInNeo4j
    public void updateFile(String treeId, String fileId, FileRequest fileRequest) {
        //TODO
    }
}
