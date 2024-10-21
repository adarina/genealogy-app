package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.exceptions.StorageException;
import com.ada.genealogyapp.file.properties.StorageProperties;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {

    private final TreeService treeService;

    private final Path rootLocation;

    private final FileRepository fileRepository;

    public FileStorageService(TreeService treeService, StorageProperties properties, FileRepository fileRepository) {
        this.treeService = treeService;
        this.fileRepository = fileRepository;
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("File upload location can not be empty");
        }
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Transactional
    public File storeFile(UUID treeId, MultipartFile multipartFile) {
        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);

        if (multipartFile.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        Path destinationFile = this.rootLocation.resolve(Paths.get(Objects.requireNonNull(multipartFile.getOriginalFilename()))).normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file");
        }

        File file = File.builder()
                .name(multipartFile.getOriginalFilename())
                .type(multipartFile.getContentType())
                .path(destinationFile.toString())
                .fileTree(tree)
                .build();

        fileRepository.save(file);
        treeService.saveTree(tree);

        log.info("File storage successfully: {}", file.getName());
        return file;
    }
}
