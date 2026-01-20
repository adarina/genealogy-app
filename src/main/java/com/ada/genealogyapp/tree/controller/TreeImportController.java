package com.ada.genealogyapp.tree.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeImportGedcomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees")
public class TreeImportController {

    private final TreeImportGedcomService treeImportGedcomService;

    private final IAuthenticationFacade authenticationFacade;

    @PostMapping(path = "/importFileGedcom", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Tree> importTreeFromFileGedcom(@RequestParam MultipartFile multipartFile) throws Throwable {
        Authentication authentication = authenticationFacade.getAuthentication();
        Tree tree = treeImportGedcomService.importTreeFile(multipartFile, authentication.getName());
        return ResponseEntity.ok(tree);
    }
}
