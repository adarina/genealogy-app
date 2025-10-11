package com.ada.genealogyapp.tree.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.dto.params.CreateTreeRequestParams;
import com.ada.genealogyapp.tree.service.TreeCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees")
public class TreeCreationController {

    private final TreeCreationService treeCreationService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping
    public ResponseEntity<?> createTree(@RequestBody TreeRequest treeRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        treeCreationService.createTree(CreateTreeRequestParams.builder()
                .userId(authentication.getName())
                .treeRequest(treeRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
