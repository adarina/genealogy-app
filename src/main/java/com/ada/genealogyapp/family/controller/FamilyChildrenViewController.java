package com.ada.genealogyapp.family.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.dto.params.GetChildParams;
import com.ada.genealogyapp.family.dto.params.GetChildrenParams;
import com.ada.genealogyapp.family.service.FamilyChildrenViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/children")
public class FamilyChildrenViewController {

    private final FamilyChildrenViewService familyChildrenViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Page<FamilyChildResponse>> getChildren(@PathVariable String treeId, @PathVariable String familyId, @PageableDefault Pageable pageable) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Page<FamilyChildResponse> childResponses = familyChildrenViewService.getChildren(GetChildrenParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .familyId(familyId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(childResponses);
    }

    @GetMapping("/{childId}")
    public ResponseEntity<FamilyChildResponse> getChild(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String childId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        FamilyChildResponse familyChildResponse = familyChildrenViewService.getChild(GetChildParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .childId(childId)
                .familyId(familyId)
                .build());
        return ResponseEntity.ok(familyChildResponse);
    }
}
