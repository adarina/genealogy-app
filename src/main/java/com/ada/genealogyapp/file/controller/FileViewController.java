package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.dto.params.GetFileParams;
import com.ada.genealogyapp.file.dto.params.GetFilesParams;
import com.ada.genealogyapp.file.service.FileViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileViewController {


    private final FileViewService fileViewService;

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getFiles(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) throws JsonProcessingException {
        Page<FileResponse> fileResponses = fileViewService.getFiles(GetFilesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(fileResponses);
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(@PathVariable String treeId, @PathVariable String fileId, @RequestHeader(value = "X-User-Id") String userId) {
        FileResponse fileResponse = fileViewService.getFile(GetFileParams.builder()
                .userId(userId)
                .treeId(treeId)
                .fileId(fileId)
                .build());
        return ResponseEntity.ok(fileResponse);
    }
}
