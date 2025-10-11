package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.dto.params.DeleteCitationParams;
import com.ada.genealogyapp.citation.dto.params.UpdateCitationRequestParams;
import com.ada.genealogyapp.citation.service.CitationManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}")
public class CitationManagementController {

    private final CitationManagementService citationManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestBody CitationRequest citationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationManagementService.updateCitation(UpdateCitationRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .citationRequest(citationRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteCitation(@PathVariable String treeId, @PathVariable String citationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationManagementService.deleteCitation(DeleteCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
