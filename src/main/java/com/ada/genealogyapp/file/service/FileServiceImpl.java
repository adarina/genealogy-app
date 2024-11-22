package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File findFileById(UUID fileId) {
        return fileRepository.findById(fileId)
                .orElseThrow(() -> new NodeNotFoundException("File not found with ID: " + fileId));
    }

    public void ensureFileExists(UUID fileId) {
        if (!fileRepository.existsById(fileId)) {
            throw new NodeNotFoundException("File not found with ID: " + fileId);
        }
    }

    @TransactionalInNeo4j
    public void saveFile(File file) {
        File savedFile = fileRepository.save(file);
        log.info("File saved successfully: {}", savedFile);
    }
}
