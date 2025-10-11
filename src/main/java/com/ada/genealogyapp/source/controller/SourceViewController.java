package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.source.dto.SourceResponse;
import com.ada.genealogyapp.source.dto.params.GetSourceParams;
import com.ada.genealogyapp.source.dto.params.GetSourcesParams;
import com.ada.genealogyapp.source.service.SourceViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources")
public class SourceViewController {

    private final SourceViewService sourceViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<SourceResponse>> getSources(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<SourceResponse> sourceResponses = sourceViewService.getSources(GetSourcesParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(sourceResponses);
    }

    @GetMapping("/{sourceId}")
    public ResponseEntity<SourceResponse> getSource(@PathVariable String treeId, @PathVariable String sourceId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        SourceResponse sourceResponse = sourceViewService.getSource(GetSourceParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .build());
        return ResponseEntity.ok(sourceResponse);
    }
}
