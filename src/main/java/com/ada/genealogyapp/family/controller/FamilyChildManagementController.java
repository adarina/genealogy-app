package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyChildManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.service.PersonManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public ResponseEntity<?> updateChildInFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID childId, @RequestBody PersonRequest personRequest) {
        personManagementService.updatePerson(treeId, childId, personRequest);
        familyChildManagementService.updateChildInFamily(treeId, familyId, childId, personRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> removeChildFromFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID childId) {
        familyChildManagementService.removeChildFromFamily(treeId, familyId, childId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping
    public ResponseEntity<?> addChildToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID childId) {
        familyChildManagementService.addChildToFamily(treeId, familyId, childId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
