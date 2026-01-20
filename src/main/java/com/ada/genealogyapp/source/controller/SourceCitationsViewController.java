package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.source.dto.SourceCitationResponse;
import com.ada.genealogyapp.source.dto.params.GetSourceCitationParams;
import com.ada.genealogyapp.source.dto.params.GetSourceCitationsParams;
import com.ada.genealogyapp.source.service.SourceCitationsViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}/citations")
public class SourceCitationsViewController {

    private final SourceCitationsViewService sourceCitationsViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<SourceCitationResponse>> getSourceCitations(@PathVariable String treeId, @PathVariable String sourceId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<SourceCitationResponse> citationsResponses = sourceCitationsViewService.getSourceCitations(GetSourceCitationsParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(citationsResponses);
    }

    @GetMapping("/{citationId}")
    public ResponseEntity<SourceCitationResponse> getSourceCitation(@PathVariable String treeId, @PathVariable String sourceId, @PathVariable String citationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        SourceCitationResponse sourceCitationResponse = sourceCitationsViewService.getSourceCitation(GetSourceCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .citationId(citationId)
                .build());
        return ResponseEntity.ok(sourceCitationResponse);
    }
}
