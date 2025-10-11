package com.ada.genealogyapp.file.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.file.dto.params.CreateMultipartFileRequestParams;
import com.ada.genealogyapp.file.service.FileCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/files")
public class FileCreationController {

    private final FileCreationService fileCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createFile(@PathVariable String treeId, @RequestParam MultipartFile multipartFile) {
        Authentication authentication = authenticationFacade.getAuthentication();
        fileCreationService.createFile(CreateMultipartFileRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .multipartFile(multipartFile)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
