package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.service.FamilyViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyViewController {

    private final FamilyViewService familyViewService;

    public FamilyViewController(FamilyViewService familyViewService) {
        this.familyViewService = familyViewService;
    }

    @GetMapping
    public ResponseEntity<Page<FamilyResponse>> getFamilies(@PathVariable UUID treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Page<FamilyResponse> familyResponses = familyViewService.getFamilies(treeId, filter, pageable);
        return ResponseEntity.ok(familyResponses);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamilyResponse> getFamily(@PathVariable UUID treeId, @PathVariable UUID familyId) {
        FamilyResponse familyResponse = familyViewService.getFamily(treeId, familyId);
        return ResponseEntity.ok(familyResponse);
    }
}
