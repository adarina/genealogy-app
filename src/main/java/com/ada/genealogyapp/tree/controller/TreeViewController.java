package com.ada.genealogyapp.tree.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.service.TreeViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees")
public class TreeViewController {

    private final TreeViewService treeViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<List<TreeResponse>> getTrees() {
        Authentication authentication = authenticationFacade.getAuthentication();
        List<TreeResponse> treeResponses = treeViewService.getTrees(authentication.getName());
        return ResponseEntity.ok(treeResponses);
    }
}
