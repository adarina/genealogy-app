package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyChildrenResponse;
import com.ada.genealogyapp.family.service.FamilyChildrenViewService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenViewController {

    private final FamilyChildrenViewService familyChildrenViewService;

    public FamilyChildrenViewController(FamilyChildrenViewService familyChildrenViewService) {
        this.familyChildrenViewService = familyChildrenViewService;
    }

    @GetMapping
    public ResponseEntity<Page<FamilyChildrenResponse>> getChildren(@PathVariable UUID treeId, @PathVariable UUID familyId, @PageableDefault Pageable pageable) {
        Page<FamilyChildrenResponse> childResponses = familyChildrenViewService.getChildren(treeId, familyId, pageable);
        return ResponseEntity.ok(childResponses);
    }
}
