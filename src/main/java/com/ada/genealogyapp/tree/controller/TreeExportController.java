package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.tree.service.TreeExportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}")
public class TreeExportController {

    private final TreeExportService treeExportService;

    public TreeExportController(TreeExportService treeExportService) {
        this.treeExportService = treeExportService;
    }

    @GetMapping("/exportJson")
    public ResponseEntity<?> exportTreeToJson(@PathVariable String treeId) throws JsonProcessingException {
        String treeJson = treeExportService.exportTreeToJson(treeId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tree_" + treeId + ".json")
                .body(treeJson);
    }

    @GetMapping("/exportGedcom")
    public ResponseEntity<?> exportTreeToGedcom(@PathVariable String treeId) {
        String treeJson = treeExportService.exportTreeToGedcom(treeId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=tree_" + treeId + ".ged")
                .body(treeJson);
    }
}
