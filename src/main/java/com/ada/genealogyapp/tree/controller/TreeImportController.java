package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.tree.dto.TreeImportGedcomRequest;
import com.ada.genealogyapp.tree.dto.TreeImportJsonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeImportGedcomService;
import com.ada.genealogyapp.tree.service.TreeImportJsonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/genealogy/trees")
public class TreeImportController {

    private final TreeImportJsonService treeImportJsonService;

    private final TreeImportGedcomService treeImportGedcomService;

    public TreeImportController(TreeImportJsonService treeImportJsonService, TreeImportGedcomService treeImportGedcomService) {
        this.treeImportJsonService = treeImportJsonService;
        this.treeImportGedcomService = treeImportGedcomService;
    }

    @PostMapping("/importJson")
    public ResponseEntity<Tree> importTreeFromJson(@RequestBody TreeImportJsonRequest importRequest) {
        Tree tree = treeImportJsonService.importTree(importRequest);
        return ResponseEntity.ok(tree);
    }

    @PostMapping("/importGedcom")
    public ResponseEntity<Tree> importTreeFromGedcom(@RequestBody TreeImportGedcomRequest importRequest) throws IOException {
        Tree tree = treeImportGedcomService.importTree(importRequest);
        return ResponseEntity.ok(tree);
    }
}
