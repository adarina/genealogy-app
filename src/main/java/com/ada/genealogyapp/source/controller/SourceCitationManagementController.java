package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.dto.params.RemoveSourceFromCitationParams;
import com.ada.genealogyapp.citation.dto.params.UpdateCitationRequestWithSourceParams;
import com.ada.genealogyapp.citation.service.CitationManagementService;
import com.ada.genealogyapp.citation.service.CitationSourceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}/citations/{citationId}")
public class SourceCitationManagementController {

    private final CitationSourceManagementService citationSourceManagementService;

    private final IAuthenticationFacade authenticationFacade;

    private final CitationManagementService citationManagementService;

    @PutMapping
    public ResponseEntity<?> updateCitationWithSource(@PathVariable String treeId, @PathVariable String sourceId, @PathVariable String citationId, @RequestBody CitationRequest sourceCitationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationManagementService.updateCitation(UpdateCitationRequestWithSourceParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .citationId(citationId)
                .citationRequest(sourceCitationRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeSourceFromCitation(@PathVariable String treeId, @PathVariable String sourceId, @PathVariable String citationId) {
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
    public ResponseEntity<?> addSourceToCitation(@PathVariable String treeId, @PathVariable String sourceId, @PathVariable String citationId, @RequestBody CitationRequest sourceCitationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationManagementService.updateCitationWithSource(UpdateCitationRequestWithSourceParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .citationId(citationId)
                .citationRequest(sourceCitationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
