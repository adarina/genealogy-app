package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.citation.service.CitationFilesViewService;
import com.ada.genealogyapp.file.dto.FileResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesViewController {

    private final CitationFilesViewService citationFilesViewService;

    public CitationFilesViewController(CitationFilesViewService citationFilesViewService) {
        this.citationFilesViewService = citationFilesViewService;
    }


    @GetMapping
    public ResponseEntity<Page<FileResponse>> getCitationFiles(@PathVariable String treeId, @PathVariable String citationId, @PageableDefault Pageable pageable) {
        Page<FileResponse> fileResponses = citationFilesViewService.getCitationFiles(treeId, citationId, pageable);
        return ResponseEntity.ok(fileResponses);
    }
}
