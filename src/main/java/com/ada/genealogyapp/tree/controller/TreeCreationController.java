package com.ada.genealogyapp.tree.controller;


import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.service.TreeCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/genealogy/tree")
public class TreeCreationController {

    private final TreeCreationService treeCreationService;

    public TreeCreationController(TreeCreationService treeCreationService) {
        this.treeCreationService = treeCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createTree(@RequestBody TreeRequest treeRequest) {
        treeCreationService.createTree(treeRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
