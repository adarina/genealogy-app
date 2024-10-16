package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.service.FamilyEventCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families/{familyId}/events")
public class FamilyEventCreationController {

    private final FamilyEventCreationService familyEventCreationService;

    public FamilyEventCreationController(FamilyEventCreationService familyEventCreationService) {
        this.familyEventCreationService = familyEventCreationService;
    }


    @PostMapping
    public ResponseEntity<?> createFamilyEvent(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody EventRequest eventRequest) {
        familyEventCreationService.createFamilyEvent(treeId, familyId, eventRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
