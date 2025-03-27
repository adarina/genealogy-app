package com.ada.genealogyapp.citation.controller;


import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.dto.params.GetCitationsParams;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations")
public class CitationViewController {

    private final CitationViewService citationViewService;

    @GetMapping
    public ResponseEntity<Page<CitationSourceResponse>> getCitations(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) throws JsonProcessingException {
        Page<CitationSourceResponse> citationResponses = citationViewService.getCitations(GetCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(citationResponses);
    }

    @GetMapping("/{citationId}")
    public ResponseEntity<CitationSourceResponse> getCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestHeader(value = "X-User-Id") String userId) {
        CitationSourceResponse citationResponse = citationViewService.getCitation(GetCitationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .build());
        return ResponseEntity.ok(citationResponse);
    }
}
