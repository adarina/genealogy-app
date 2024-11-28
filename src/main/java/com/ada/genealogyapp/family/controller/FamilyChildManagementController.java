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
    public ResponseEntity<?> updateChildInFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest familyChildRequest) {
        personManagementService.updatePerson(treeId, childId, familyChildRequest);
        familyChildManagementService.updateChildInFamily(treeId, familyId, childId, familyChildRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeChildFromFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId) {
        familyChildManagementService.removeChildFromFamily(treeId, familyId, childId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addChildToFamily(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestBody FamilyChildRequest personRequest) {
        familyChildManagementService.addChildToFamily(treeId, familyId, childId, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}