package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilySearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families")
public class FamilySearchController {

    private final FamilySearchService familySearchService;

    public FamilySearchController(FamilySearchService familySearchService) {
        this.familySearchService = familySearchService;
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<Family> getFamily(@PathVariable UUID familyId, @PathVariable String treeId) {
        Family family = familySearchService.findFamilyById(familyId);
        return ResponseEntity.ok(family);
    }

    @GetMapping
    public ResponseEntity<List<Family>> getFamiliesByTree(@PathVariable UUID treeId) {
        List<Family> families = familySearchService.getFamiliesByTreeId(treeId);
        return ResponseEntity.ok(families);
    }
}
