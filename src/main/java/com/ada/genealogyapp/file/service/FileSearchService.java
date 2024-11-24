package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FileSearchService {

    private final FileRepository fileRepository;

    public FileSearchService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File findFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new NodeNotFoundException("File not found with ID: " + fileId));
    }
}
