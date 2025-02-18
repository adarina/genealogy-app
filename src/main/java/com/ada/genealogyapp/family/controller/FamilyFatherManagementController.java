package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyFatherManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/father/{fatherId}")
public class FamilyFatherManagementController {

    private final FamilyFatherManagementService familyFatherManagementService;

    public FamilyFatherManagementController(FamilyFatherManagementService familyFatherManagementService) {
        this.familyFatherManagementService = familyFatherManagementService;
    }

    @DeleteMapping
    public ResponseEntity<?> removeFatherFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String fatherId) {
        familyFatherManagementService.removeFatherFromFamily(treeId, familyId, fatherId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addFatherToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String fatherId) {
        familyFatherManagementService.addFatherToFamily(treeId, familyId, fatherId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
