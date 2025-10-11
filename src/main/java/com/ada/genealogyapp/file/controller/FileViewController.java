package com.ada.genealogyapp.file.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.dto.params.GetFileParams;
import com.ada.genealogyapp.file.dto.params.GetFilesParams;
import com.ada.genealogyapp.file.service.FileViewService;
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
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileViewController {

    private final FileViewService fileViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<FileResponse>> getFiles(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<FileResponse> fileResponses = fileViewService.getFiles(GetFilesParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(fileResponses);
    }


    @GetMapping("/{fileId}")
    public ResponseEntity<FileResponse> getFile(@PathVariable String treeId, @PathVariable String fileId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        FileResponse fileResponse = fileViewService.getFile(GetFileParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .fileId(fileId)
                .build());
        return ResponseEntity.ok(fileResponse);
    }
}
