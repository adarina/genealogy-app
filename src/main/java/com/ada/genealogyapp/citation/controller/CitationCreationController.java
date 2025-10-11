package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.CreateCitationRequestParams;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations")
public class CitationCreationController {

    private final CitationCreationService citationCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createCitation(@PathVariable String treeId, @RequestBody CitationRequest citationRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        citationCreationService.createCitation(CreateCitationRequestParams.builder()
                .treeId(treeId)
                .userId(authentication.getName())
                .citationRequest(citationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
