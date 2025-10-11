package com.ada.genealogyapp.citation.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.service.CitationDateViewService;
import com.ada.genealogyapp.date.model.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/citations/{citationId}/date")
public class CitationDateViewController {

    private final CitationDateViewService citationDateViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Date> getCitationDate(@PathVariable String treeId, @PathVariable String citationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Date citationDateResponse = citationDateViewService.getCitationDate(GetCitationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .citationId(citationId)
                .build());
        return ResponseEntity.ok(citationDateResponse);
    }
}
