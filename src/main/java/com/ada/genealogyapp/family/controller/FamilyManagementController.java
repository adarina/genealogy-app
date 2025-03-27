package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.DeleteFamilyParams;
import com.ada.genealogyapp.family.dto.params.UpdateFamilyRequestParams;
import com.ada.genealogyapp.family.service.FamilyManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}")
public class FamilyManagementController {

    private final FamilyManagementService familyManagementService;

    @PutMapping
    public ResponseEntity<?> updateFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestBody FamilyRequest familyRequest, @RequestHeader(value = "X-User-Id") String userId) {
        familyManagementService.updateFamily(UpdateFamilyRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .familyRequest(familyRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestHeader(value = "X-User-Id") String userId) {
        familyManagementService.deleteFamily(DeleteFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
