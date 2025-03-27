package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileRequestParams;
import com.ada.genealogyapp.file.service.FileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/files/{fileId}")
public class FileManagementController {

    private final FileManagementService fileManagementService;

    @PutMapping
    public ResponseEntity<?> updateFile(@PathVariable String treeId, @PathVariable String fileId, @RequestBody FileRequest fileRequest, @RequestHeader(value = "X-User-Id") String userId) {
        fileManagementService.updateFile(UpdateFileRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .fileId(fileId)
                .fileRequest(fileRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@PathVariable String treeId, @PathVariable String fileId, @RequestHeader(value = "X-User-Id") String userId) {
        fileManagementService.deleteFile(DeleteFileParams.builder()
                .userId(userId)
                .treeId(treeId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}