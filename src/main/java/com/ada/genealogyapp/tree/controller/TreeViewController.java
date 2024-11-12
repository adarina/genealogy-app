package com.ada.genealogyapp.tree.controller;


import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.service.TreeViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/genealogy/trees")
public class TreeViewController {

    private final TreeViewService treeViewService;

    public TreeViewController(TreeViewService treeViewService) {
        this.treeViewService = treeViewService;
    }

    @GetMapping
    public ResponseEntity<List<TreeResponse>> getTrees() {
        List<TreeResponse> treeResponses = treeViewService.getTrees();
        return ResponseEntity.ok(treeResponses);
    }
}

