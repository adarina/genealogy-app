package com.ada.genealogyapp.source.controller;

import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.DeleteSourceParams;
import com.ada.genealogyapp.source.dto.params.UpdateSourceRequestParams;
import com.ada.genealogyapp.source.service.SourceManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/sources/{sourceId}")
public class SourceManagementController {

    private final SourceManagementService sourceManagementService;

    @PutMapping
    public ResponseEntity<?> updateSource(@PathVariable String treeId, @PathVariable String sourceId, @RequestBody SourceRequest sourceRequest, @RequestHeader(value = "X-User-Id") String userId) {
        sourceManagementService.updateSource(UpdateSourceRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .sourceRequest(sourceRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteSource(@PathVariable String treeId, @PathVariable String sourceId, @RequestHeader(value = "X-User-Id") String userId) {
        sourceManagementService.deleteSource(DeleteSourceParams.builder()
                .userId(userId)
                .treeId(treeId)
                .sourceId(sourceId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}