package com.ada.genealogyapp.tree.controller;


import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeImportGedcomService;
import com.ada.genealogyapp.tree.service.TreeImportJsonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees")
public class TreeImportController {

    private final TreeImportJsonService treeImportJsonService;

    private final TreeImportGedcomService treeImportGedcomService;

    @PostMapping(path = "/importFileGedcom", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Tree> importTreeFromFileGedcom(@RequestParam MultipartFile multipartFile, @RequestHeader(value = "X-User-Id") String userId) throws Throwable {
        Tree tree = treeImportGedcomService.importTreeFile(multipartFile, userId);
        return ResponseEntity.ok(tree);
    }

    @PostMapping(path = "/importFileJson", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Tree> importTreeFromFileJson(@RequestParam MultipartFile multipartFile, @RequestHeader(value = "X-User-Id") String userId) throws Throwable {
        Tree tree = treeImportJsonService.importTreeFile(multipartFile, userId);
        return ResponseEntity.ok(tree);
    }
}
