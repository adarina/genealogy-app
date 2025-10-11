package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.AddSourceToCitationParams;
import com.ada.genealogyapp.citation.dto.params.RemoveSourceFromCitationParams;
import com.ada.genealogyapp.citation.service.CitationSourceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/source/{sourceId}")
public class CitationSourceManagementController {

    private final CitationSourceManagementService citationSourceManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @DeleteMapping
    public ResponseEntity<?> removeSourceFromCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String sourceId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationSourceManagementService.removeSourceFromCitation(RemoveSourceFromCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .sourceId(sourceId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addSourceToCitation(@PathVariable String treeId, @PathVariable String citationId, @PathVariable String sourceId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationSourceManagementService.addSourceToCitation(AddSourceToCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .sourceId(sourceId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
