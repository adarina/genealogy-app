package com.ada.genealogyapp.file.controller;


import com.ada.genealogyapp.file.dto.params.CreateMultipartFileRequestParams;
import com.ada.genealogyapp.file.service.FileCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileCreationController {

    private final FileCreationService fileCreationService;

    @PostMapping
    public ResponseEntity<?> createFile(@PathVariable String treeId, @RequestParam MultipartFile multipartFile, @RequestHeader(value = "X-User-Id") String userId) {
        fileCreationService.createFile(CreateMultipartFileRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .multipartFile(multipartFile)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
