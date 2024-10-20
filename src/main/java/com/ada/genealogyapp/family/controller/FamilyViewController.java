package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.service.FamilyViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyViewController {

    private final FamilyViewService familyViewService;

    public FamilyViewController(FamilyViewService familyViewService) {
        this.familyViewService = familyViewService;
    }

    @GetMapping
    public ResponseEntity<List<FamilyResponse>> getFamilies(@PathVariable UUID treeId) {
        List<FamilyResponse> familyResponses = familyViewService.getFamilies(treeId);
        return ResponseEntity.ok(familyResponses);
    }
}
