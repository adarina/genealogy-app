package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
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
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families")
public class FamilyViewController {

    private final FamilyViewService familyViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<FamiliesResponse>> getFamilies(@PathVariable String treeId, @RequestParam String filter, @PageableDefault Pageable pageable) throws JsonProcessingException {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<FamiliesResponse> familyResponses = familyViewService.getFamilies(GetFamiliesParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(familyResponses);
    }

    @GetMapping("/{familyId}")
    public ResponseEntity<FamiliesResponse> getFamily(@PathVariable String treeId, @PathVariable String familyId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        FamiliesResponse familiesResponse = familyViewService.getFamily(GetFamilyParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .build());
        return ResponseEntity.ok(familiesResponse);
    }
}
