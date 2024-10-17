package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families")
public class FamilySearchController {

    private final FamilyService familyService;

    public FamilySearchController(FamilyService familyService) {
        this.familyService = familyService;
    }


    @GetMapping("/{familyId}")
    public ResponseEntity<Family> getFamily(@PathVariable UUID familyId, @PathVariable String treeId) {
        Family family = familyService.findFamilyByIdOrThrowNodeNotFoundException(familyId);
        return ResponseEntity.ok(family);
    }

    @GetMapping
    public ResponseEntity<List<Family>> getFamiliesByTree(@PathVariable UUID treeId) {
        List<Family> families = familyService.getFamiliesByTreeId(treeId);
        return ResponseEntity.ok(families);
    }
}
