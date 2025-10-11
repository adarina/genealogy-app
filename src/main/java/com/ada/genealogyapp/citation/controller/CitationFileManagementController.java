package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.dto.params.RemoveFileFromCitationParams;
import com.ada.genealogyapp.citation.service.CitationFileManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files/{fileId}")
public class CitationFileManagementController {

    private final CitationFileManagementService citationFileManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> addFileToCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String fileId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationFileManagementService.addFileToCitation(AddFileToCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeFileFromCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String fileId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationFileManagementService.removeFileFromCitation(RemoveFileFromCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .fileId(fileId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
