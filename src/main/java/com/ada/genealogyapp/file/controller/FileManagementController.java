package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.dto.params.DeleteFileParams;
import com.ada.genealogyapp.file.dto.params.UpdateFileRequestParams;
import com.ada.genealogyapp.file.service.FileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/files/{fileId}")
public class FileManagementController {

    private final FileManagementService fileManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateFile(@PathVariable String treeId, @PathVariable String fileId, @RequestBody FileRequest fileRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        fileManagementService.updateFile(UpdateFileRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .fileId(fileId)
                .fileRequest(fileRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFile(@PathVariable String treeId, @PathVariable String fileId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        fileManagementService.deleteFile(DeleteFileParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
