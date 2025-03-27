package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.person.dto.params.UpdateChildInFamilyRequestParams;
import com.ada.genealogyapp.person.service.PersonManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children/{childId}")
public class FamilyChildManagementController {

    private final FamilyChildManagementService familyChildManagementService;

    private final PersonManagementService personManagementService;

    @PutMapping
    public ResponseEntity<?> updateChildInFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest familyChildRequest, @RequestHeader(value = "X-User-Id") String userId) {
        personManagementService.updateChildInFamily(UpdateChildInFamilyRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .personId(childId)
                .familyId(familyId)
                .familyChildRequest(familyChildRequest)
                .personRequest(familyChildRequest)
                .build());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeChildFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestHeader(value = "X-User-Id") String userId) {
        familyChildManagementService.removeChildFromFamily(RemovePersonFromFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(childId)
                .build());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest familyChildRequest, @RequestHeader(value = "X-User-Id") String userId) {
        familyChildManagementService.addChildToFamily(AddChildToFamilyRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .personId(childId)
                .familyChildRequest(familyChildRequest)
                .build());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}