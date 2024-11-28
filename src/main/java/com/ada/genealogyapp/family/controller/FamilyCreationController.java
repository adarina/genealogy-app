package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyCreationController {

    private final FamilyCreationService familyCreationService;

    public FamilyCreationController(FamilyCreationService familyCreationService) {
        this.familyCreationService = familyCreationService;
    }

    @PostMapping
    public ResponseEntity<?> createFamily(@PathVariable String treeId, @RequestBody FamilyRequest familyRequest) {
        familyCreationService.createFamily(treeId, familyRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
