package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.service.FamilyManagementService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families/{familyId}")
public class FamilyManagementController {

    private final FamilyManagementService familyManagementService;

    public FamilyManagementController(FamilyManagementService familyManagementService) {
        this.familyManagementService = familyManagementService;
    }

    @PostMapping("/person/new")
    public ResponseEntity<?> addNewPersonToFamily(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {

        familyManagementService.addPersonToFamily(familyId, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
