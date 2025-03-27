package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.family.service.FamilyMotherManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/mother/{motherId}")
public class FamilyMotherManagementController {

    private final FamilyMotherManagementService familyMotherManagementService;

    @DeleteMapping
    public ResponseEntity<?> removePersonFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId, @RequestHeader(value = "X-User-Id") String userId) {
        familyMotherManagementService.removeMotherFromFamily(RemovePersonFromFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(motherId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<String> addMotherToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String motherId, @RequestHeader(value = "X-User-Id") String userId) {
        familyMotherManagementService.addMotherToFamily(AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(motherId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
