package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.service.TreeViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees")
public class TreeViewController {

    private final TreeViewService treeViewService;

    @GetMapping
    public ResponseEntity<List<TreeResponse>> getTrees(@RequestHeader(value = "X-User-Id") String userId) {
        List<TreeResponse> treeResponses = treeViewService.getTrees(userId);
        return ResponseEntity.ok(treeResponses);
    }
}