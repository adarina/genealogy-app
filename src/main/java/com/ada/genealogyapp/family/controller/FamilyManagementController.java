package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.service.FamilyManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}")
public class FamilyManagementController {

    private final FamilyManagementService familyManagementService;

    public FamilyManagementController(FamilyManagementService familyManagementService) {
        this.familyManagementService = familyManagementService;
    }


    @PostMapping()
    public ResponseEntity<Void> startEditingFamily(@PathVariable UUID treeId, @PathVariable UUID familyId) {
//        familyManagementService.startTransactionAndSession(treeId, familyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/cancel")
    public ResponseEntity<Void> cancelEditingFamily(@PathVariable UUID treeId, @PathVariable UUID familyId) {
//        familyManagementService.rollbackChanges(treeId, familyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/commit")
    public ResponseEntity<Void> commitEditingFamily(@PathVariable UUID treeId, @PathVariable UUID familyId) {
//        familyManagementService.commitChanges(treeId,familyId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
