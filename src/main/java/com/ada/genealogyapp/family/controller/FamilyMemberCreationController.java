package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.service.FamilyMemberCreationService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families/{familyId}/members")
public class FamilyMemberCreationController {

    private final FamilyMemberCreationService familyMemberCreationService;

    public FamilyMemberCreationController(FamilyMemberCreationService familyMemberCreationService) {
        this.familyMemberCreationService = familyMemberCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createFamilyMember(@PathVariable UUID treeId, @PathVariable UUID familyId, @RequestBody PersonRequest personRequest) {
        familyMemberCreationService.createFamilyMember(treeId, familyId, personRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
