package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.service.FamilyManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}")
public class FamilyManagementController {

    private final FamilyManagementService familyManagementService;

    public FamilyManagementController(FamilyManagementService familyManagementService) {
        this.familyManagementService = familyManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyRequest familyRequest, @RequestHeader(value = "X-User-Id") String userId) {
        familyManagementService.updateFamily(userId, treeId, familyId, familyRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestHeader(value = "X-User-Id") String userId) {
        familyManagementService.deleteFamily(userId, treeId, familyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
