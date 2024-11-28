package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyMotherManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/mother/{motherId}")
public class FamilyMotherManagementController {

    private final FamilyMotherManagementService familyMotherManagementService;

    public FamilyMotherManagementController(FamilyMotherManagementService familyMotherManagementService) {
        this.familyMotherManagementService = familyMotherManagementService;
    }

    @DeleteMapping
    public ResponseEntity<?> removePersonFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId) {
        familyMotherManagementService.removeMotherFromFamily(treeId, familyId, motherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addMotherToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId) {
        familyMotherManagementService.addMotherToFamily(treeId, familyId, motherId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
