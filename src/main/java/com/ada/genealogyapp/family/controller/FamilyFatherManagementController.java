package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.family.service.FamilyFatherManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/father/{fatherId}")
public class FamilyFatherManagementController {

    private final FamilyFatherManagementService familyFatherManagementService;

    @DeleteMapping
    public ResponseEntity<?> removeFatherFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String fatherId, @RequestHeader(value = "X-User-Id") String userId) {
        familyFatherManagementService.removeFatherFromFamily(RemovePersonFromFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(fatherId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addFatherToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String fatherId, @RequestHeader(value = "X-User-Id") String userId) {
        familyFatherManagementService.addFatherToFamily(AddPersonToFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(fatherId)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
