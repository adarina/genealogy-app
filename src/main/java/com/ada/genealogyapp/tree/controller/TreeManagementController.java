package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.dto.params.DeleteTreeParams;
import com.ada.genealogyapp.tree.dto.params.UpdateTreeRequestParams;
import com.ada.genealogyapp.tree.service.TreeManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}")
public class TreeManagementController {

    private final TreeManagementService treeManagementService;

    private final IAuthenticationFacade authenticationFacade;

    @PutMapping
    public ResponseEntity<?> updateTree(@PathVariable String treeId, @RequestBody TreeRequest treeRequest) {
        Authentication authentication = authenticationFacade.getAuthentication();
        treeManagementService.updateTree(UpdateTreeRequestParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .treeRequest(treeRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTree(@PathVariable String treeId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        treeManagementService.deleteTree(DeleteTreeParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
