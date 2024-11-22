package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileViewController {


    private final FileViewService fileViewService;

    public FileViewController(FileViewService fileViewService) {
        this.fileViewService = fileViewService;
    }

    @GetMapping
    public ResponseEntity<List<File>> getFiles(@PathVariable UUID treeId) {
        List<File> files = fileViewService.getFiles(treeId);
        return ResponseEntity.ok(files);
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(@PathVariable UUID treeId, @PathVariable UUID fileId) {
        FileResponse fileResponse = fileViewService.getFile(treeId, fileId);
        return ResponseEntity.ok(fileResponse);
    }
}
