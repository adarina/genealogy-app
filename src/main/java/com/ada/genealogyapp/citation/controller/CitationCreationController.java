package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.dto.params.CreateCitationRequestParams;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations")
public class CitationCreationController {

    private final CitationCreationService citationCreationService;

    @PostMapping
    public ResponseEntity<?> createCitation(@PathVariable String treeId, @RequestBody CitationRequest citationRequest, @RequestHeader(value = "X-User-Id") String userId) {
        citationCreationService.createCitation(CreateCitationRequestParams.builder()
                .treeId(treeId)
                .userId(userId)
                .citationRequest(citationRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
