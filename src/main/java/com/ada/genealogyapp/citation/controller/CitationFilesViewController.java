package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.dto.params.GetCitationFilesParams;
import com.ada.genealogyapp.citation.service.CitationFilesViewService;
import com.ada.genealogyapp.file.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesViewController {

    private final CitationFilesViewService citationFilesViewService;

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getCitationFiles(@PathVariable String treeId, @PathVariable String citationId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<FileResponse> fileResponses = citationFilesViewService.getCitationFiles(GetCitationFilesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .citationId(citationId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(fileResponses);
    }
}
