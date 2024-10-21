package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileSearchService {

    private final FileRepository fileRepository;

    public FileSearchService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public List<File> getFilesByTreeIdOrThrowNodeNotFoundException(UUID treeId) {
        List<File> files = fileRepository.findAllByFileTree_Id(treeId);
        if (!files.isEmpty()) {
            log.info("Files found for treeId {}: {}", treeId, files);
        } else {
            log.error("No files found for treeId: {}", treeId);
            throw new NodeNotFoundException("No files found for treeId: " + treeId);
        }
        return files;
    }
}
