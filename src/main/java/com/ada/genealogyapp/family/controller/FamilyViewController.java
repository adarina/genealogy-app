package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.family.dto.FamiliesResponse;
import com.ada.genealogyapp.family.dto.params.GetFamiliesParams;
import com.ada.genealogyapp.family.dto.params.GetFamilyParams;
import com.ada.genealogyapp.family.service.FamilyViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyViewController {

    private final FamilyViewService familyViewService;

    @GetMapping
    public ResponseEntity<Page<FamiliesResponse>> getFamilies(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) throws JsonProcessingException {
        Page<FamiliesResponse> familyResponses = familyViewService.getFamilies(GetFamiliesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(familyResponses);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamiliesResponse> getFamily(@PathVariable String treeId, @PathVariable String familyId, @RequestHeader(value = "X-User-Id") String userId) {
        FamiliesResponse familiesResponse = familyViewService.getFamily(GetFamilyParams.builder()
                .userId(userId)
                .treeId(treeId)
                .familyId(familyId)
                .build());
        return ResponseEntity.ok(familiesResponse);
    }
}
