package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationFileManagementService;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesManagementController {
    private final CitationFileManagementService citationFileManagementService;

    private final FileStorageService fileStorageService;

    public CitationFilesManagementController(CitationFileManagementService citationFileManagementService, FileStorageService fileStorageService) {
        this.citationFileManagementService = citationFileManagementService;
        this.fileStorageService = fileStorageService;
    }


    @PostMapping
    public ResponseEntity<?> createAndAddFileToCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestParam MultipartFile multipartFile, @RequestHeader(value = "X-User-Id") String userId) {
        File file = fileStorageService.storeFile(userId, treeId, multipartFile);
        citationFileManagementService.addFileToCitation(userId, treeId, citationId, file.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}