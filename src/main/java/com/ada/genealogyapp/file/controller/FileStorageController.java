package com.ada.genealogyapp.file.controller;


import com.ada.genealogyapp.file.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileStorageController {

    private final FileStorageService fileStorageService;

    public FileStorageController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @PostMapping
    public ResponseEntity<?> storeFile(@PathVariable String treeId, @RequestParam MultipartFile file, @RequestHeader(value = "X-User-Id") String userId) {
        fileStorageService.storeFile(userId, treeId, file);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
