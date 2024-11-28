package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.service.FileManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/files/{fileId}")
public class FileManagementController {

    private final FileManagementService fileManagementService;

    public FileManagementController(FileManagementService fileManagementService) {
        this.fileManagementService = fileManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateFile(@PathVariable String treeId, @PathVariable String fileId, @RequestBody FileRequest fileRequest) {
        fileManagementService.updateFile(treeId, fileId, fileRequest);
        return ResponseEntity.ok().build();
    }
}
