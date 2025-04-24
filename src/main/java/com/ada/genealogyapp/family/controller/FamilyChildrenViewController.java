package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.dto.params.GetChildParams;
import com.ada.genealogyapp.family.dto.params.GetChildrenParams;
import com.ada.genealogyapp.family.service.FamilyChildrenViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenViewController {

    private final FamilyChildrenViewService familyChildrenViewService;

    @GetMapping
    public ResponseEntity<Page<FamilyChildResponse>> getChildren(@PathVariable String treeId, @PathVariable String familyId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<FamilyChildResponse> childResponses = familyChildrenViewService.getChildren(GetChildrenParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(childResponses);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<FamilyChildResponse> getChild(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId, @RequestHeader(value = "X-User-Id") String userId) {
        FamilyChildResponse familyChildResponse = familyChildrenViewService.getChild(GetChildParams.builder()
                .userId(userId)
                .treeId(treeId)
                .childId(childId)
                .familyId(familyId)
                .build());
        return ResponseEntity.ok(familyChildResponse);
    }
}