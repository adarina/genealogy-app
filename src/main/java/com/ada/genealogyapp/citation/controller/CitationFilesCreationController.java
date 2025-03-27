package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.file.dto.params.CreateAndAddMultipartFileToCitationRequestParams;
import com.ada.genealogyapp.file.service.FileCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/files")
public class CitationFilesCreationController {

    private final FileCreationService fileCreationService;

    @PostMapping
    public ResponseEntity<?> createAndAddFileToCitation(@PathVariable String treeId, @PathVariable String citationId, @RequestParam MultipartFile multipartFile, @RequestHeader(value = "X-User-Id") String userId) {
        fileCreationService.createAndAddFileToCitation(CreateAndAddMultipartFileToCitationRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .multipartFile(multipartFile)
                .citationId(citationId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}