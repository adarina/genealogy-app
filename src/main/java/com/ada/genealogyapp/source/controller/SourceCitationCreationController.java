package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.dto.params.CreateCitationRequestWithSourceParams;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}/citations")
public class SourceCitationCreationController {

    private final CitationCreationService citationCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createCitationWithSource(@PathVariable String treeId, @PathVariable String sourceId, @RequestBody CitationRequest sourceCitationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationCreationService.createCitationWithSource(CreateCitationRequestWithSourceParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .citationRequest(sourceCitationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
