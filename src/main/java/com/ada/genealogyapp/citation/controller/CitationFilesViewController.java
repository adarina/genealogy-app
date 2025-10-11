package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.GetCitationFilesParams;
import com.ada.genealogyapp.citation.service.CitationFilesViewService;
import com.ada.genealogyapp.file.dto.FileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesViewController {

    private final CitationFilesViewService citationFilesViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getCitationFiles(@PathVariable String treeId, @PathVariable String citationId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<FileResponse> fileResponses = citationFilesViewService.getCitationFiles(GetCitationFilesParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(fileResponses);
    }
}
