package com.ada.genealogyapp.file.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.exceptions.StorageException;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.repository.FileRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
    private final FileRepository fileRepository;

    private final  FileService fileService;

    @Value("${file.storage}")
    private String storage;

    public FileStorageService(TreeService treeService, FileRepository fileRepository, FileService fileService) {
        this.treeService = treeService;
        this.fileRepository = fileRepository;
        this.fileService = fileService;
    }

    @TransactionalInNeo4j
    public File storeFile(String treeId, MultipartFile multipartFile) {
        if (this.storage.trim().isEmpty()) {
            throw new StorageException("File upload location can not be empty");
        }
        Path rootLocation = Paths.get(this.storage);

        if (multipartFile.isEmpty()) {
            throw new StorageException("Failed to store empty file");
        }

        Path destinationFile = rootLocation.resolve(Paths.get(Objects.requireNonNull(multipartFile.getOriginalFilename()))).normalize().toAbsolutePath();
        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new StorageException("Cannot store file outside current directory");
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new StorageException("Failed to store file");
        }

        File file = File.builder()
                .id(UUID.randomUUID().toString())
                .name(multipartFile.getOriginalFilename())
                .type(multipartFile.getContentType())
                .path(destinationFile.toString())
                .filename(multipartFile.getOriginalFilename())
                .build();

        fileService.saveFile(treeId, file);
        return file;
    }
}
