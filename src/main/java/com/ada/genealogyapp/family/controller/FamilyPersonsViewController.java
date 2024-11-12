package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.family.dto.FamilyParentResponse;
import com.ada.genealogyapp.family.service.FamilyPersonsViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/persons")
public class FamilyPersonsViewController {

    private final FamilyPersonsViewService familyPersonsViewService;

    public FamilyPersonsViewController(FamilyPersonsViewService familyPersonsViewService) {
        this.familyPersonsViewService = familyPersonsViewService;
    }

    @GetMapping("/father")
    public ResponseEntity<FamilyParentResponse> getFatherInformation(@PathVariable UUID treeId, @PathVariable UUID familyId) {
        FamilyParentResponse fatherInfo = familyPersonsViewService.getFatherInformation(treeId, familyId);
        return ResponseEntity.ok(fatherInfo);
    }

    @GetMapping("/mother")
    public ResponseEntity<FamilyParentResponse> getMotherInformation(@PathVariable UUID treeId, @PathVariable UUID familyId) {
        FamilyParentResponse motherInfo = familyPersonsViewService.getMotherInformation(treeId, familyId);
        return ResponseEntity.ok(motherInfo);
    }
}
