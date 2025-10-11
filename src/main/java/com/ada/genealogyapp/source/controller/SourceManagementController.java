package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.DeleteSourceParams;
import com.ada.genealogyapp.source.dto.params.UpdateSourceRequestParams;
import com.ada.genealogyapp.source.service.SourceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}")
public class SourceManagementController {

    private final SourceManagementService sourceManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateSource(@PathVariable String treeId, @PathVariable String sourceId, @RequestBody SourceRequest sourceRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        sourceManagementService.updateSource(UpdateSourceRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .sourceRequest(sourceRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSource(@PathVariable String treeId, @PathVariable String sourceId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        sourceManagementService.deleteSource(DeleteSourceParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .sourceId(sourceId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
