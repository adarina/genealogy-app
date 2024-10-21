package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationFilesManagementService;
import com.ada.genealogyapp.file.dto.FileCitationRequest;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileStorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesManagementController {
    private final CitationFilesManagementService citationFilesManagementService;

    private final FileStorageService fileStorageService;

    public CitationFilesManagementController(CitationFilesManagementService citationFilesManagementService, FileStorageService fileStorageService) {
        this.citationFilesManagementService = citationFilesManagementService;
        this.fileStorageService = fileStorageService;
    }


    @PostMapping("/addExistingFile")
    public ResponseEntity<?> addExistingFile(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestBody FileCitationRequest fileCitationRequest) {
        citationFilesManagementService.addExistingFileToCitation(treeId, citationId, fileCitationRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewFile")
    public ResponseEntity<?> addNewFile(@PathVariable UUID treeId, @PathVariable UUID citationId, @RequestParam MultipartFile multipartFile) {
        File file = fileStorageService.storeFile(treeId, multipartFile);
        citationFilesManagementService.addExistingFileToCitation(treeId, citationId, file.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}