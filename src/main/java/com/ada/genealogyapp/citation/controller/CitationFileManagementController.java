package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.dto.params.RemoveFileFromCitationParams;
import com.ada.genealogyapp.citation.service.CitationFileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files/{fileId}")
public class CitationFileManagementController {

    private final CitationFileManagementService citationFileManagementService;

    @PostMapping
    public ResponseEntity<?> addFileToCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String fileId, @RequestHeader(value = "X-User-Id") String userId) {
        citationFileManagementService.addFileToCitation(AddFileToCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFileFromCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String fileId, @RequestHeader(value = "X-User-Id") String userId) {
        citationFileManagementService.removeFileFromCitation(RemoveFileFromCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
