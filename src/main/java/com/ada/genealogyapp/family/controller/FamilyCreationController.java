package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.service.FamilyCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/tree/{treeId}/families")
public class FamilyCreationController {

    private final FamilyCreationService familyCreationService;

    public FamilyCreationController(FamilyCreationService familyCreationService) {
        this.familyCreationService = familyCreationService;
    }


    @PostMapping
    public ResponseEntity<?> createFamily(@PathVariable UUID treeId) {
        familyCreationService.createFamily(treeId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
