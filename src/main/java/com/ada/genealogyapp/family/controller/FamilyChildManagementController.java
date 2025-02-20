package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.person.service.PersonManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children/{childId}")
public class FamilyChildManagementController {

    private final FamilyChildManagementService familyChildManagementService;
    private final PersonManagementService personManagementService;

    public FamilyChildManagementController(FamilyChildManagementService familyChildManagementService, PersonManagementService personManagementService) {
        this.familyChildManagementService = familyChildManagementService;
        this.personManagementService = personManagementService;
    }

    @PutMapping
    public ResponseEntity<?> updateChildInFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest familyChildRequest, @RequestHeader(value = "X-User-Id") String userId) {
        personManagementService.updatePerson(userId, treeId, childId, familyChildRequest);
        familyChildManagementService.updateChildInFamily(userId, treeId, familyId, childId, familyChildRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeChildFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestHeader(value = "X-User-Id") String userId) {
        familyChildManagementService.removeChildFromFamily(userId, treeId, familyId, childId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest personRequest, @RequestHeader(value = "X-User-Id") String userId) {
        familyChildManagementService.addChildToFamily(userId, treeId, familyId, childId, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}