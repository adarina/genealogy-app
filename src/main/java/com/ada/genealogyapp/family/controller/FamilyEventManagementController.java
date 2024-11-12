package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyEventManagementService;
import com.ada.genealogyapp.person.dto.PersonEventRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events/{eventId}")
public class FamilyEventManagementController {

    private final FamilyEventManagementService familyEventManagementService;

    public FamilyEventManagementController(FamilyEventManagementService familyEventManagementService) {
        this.familyEventManagementService = familyEventManagementService;
    }

    @PutMapping()
    public ResponseEntity<?> updateFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId, @RequestBody PersonEventRequest eventRequest) {
        familyEventManagementService.updateFamilyEvent(treeId, familyId, eventId, eventRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeEventFromFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @PathVariable UUID eventId) {
        familyEventManagementService.removeEventFromFamily(treeId, familyId, eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}

