package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.service.FileViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileViewController {


    private final FileViewService fileViewService;

    public FileViewController(FileViewService fileViewService) {
        this.fileViewService = fileViewService;
    }

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getFiles(@PathVariable UUID treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<FileResponse> fileResponses = fileViewService.getFiles(treeId, filter, pageable);
        return ResponseEntity.ok(fileResponses);
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(@PathVariable UUID treeId, @PathVariable UUID fileId) {
        FileResponse fileResponse = fileViewService.getFile(treeId, fileId);
        return ResponseEntity.ok(fileResponse);
    }
}
