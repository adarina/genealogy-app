package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.tree.dto.TreeExportJsonResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.tree.service.TreeExportGedcomService;
import com.ada.genealogyapp.tree.service.TreeExportJsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/export")
public class TreeExportController {

    private final TreeExportGedcomService gedcomService;

    private final TreeExportJsonService jsonService;

    private final ObjectMapper objectMapper;

    @GetMapping("/json")
    public ResponseEntity<Resource> exportTreeToJsonFile(@PathVariable String treeId, @RequestHeader(value = "X-User-Id") String userId) throws IOException {
        TreeExportJsonResponse treeJson = (TreeExportJsonResponse) jsonService.exportTree(BaseParams.builder()
                .userId(userId)
                .treeId(treeId)
                .build());

        String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(treeJson);
        byte[] jsonBytes = jsonString.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(jsonBytes);
        String fileName = "tree_" + treeId + ".json";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(jsonBytes.length)
                .body(resource);
    }

    @GetMapping("/gedcom")
    public ResponseEntity<Resource> exportTreeToGedcomFile(@PathVariable String treeId, @RequestHeader(value = "X-User-Id") String userId) {
        String gedcomContent = (String) gedcomService.exportTree(BaseParams.builder()
                .userId(userId)
                .treeId(treeId)
                .build());

        byte[] gedcomBytes = gedcomContent.getBytes(StandardCharsets.UTF_8);
        ByteArrayResource resource = new ByteArrayResource(gedcomBytes);
        String fileName = "tree_" + treeId + ".ged";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.TEXT_PLAIN)
                .contentLength(gedcomBytes.length)
                .body(resource);
    }
}
