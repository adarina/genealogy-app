package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children/{childId}")
public class FamilyChildManagementController {


    private final FamilyChildManagementService familyChildManagementService;

    public FamilyChildManagementController(FamilyChildManagementService familyChildManagementService) {
        this.familyChildManagementService = familyChildManagementService;
    }

    @PutMapping()
    public ResponseEntity<?> updateFamilyChild(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID childId, @RequestBody FamilyChildRequest familyChildRequest) {
        familyChildManagementService.updateFamilyChild(treeId, familyId, childId, familyChildRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeChildFromFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID childId) {
        familyChildManagementService.removeChildFromFamily(treeId, familyId, childId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
