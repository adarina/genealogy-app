package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.GeographyResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.service.PersonGeographyViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/geographies")
public class PersonGeographiesViewController {

    private final PersonGeographyViewService personGeographiesViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<List<GeographyResponse>> getPersonGeographies(@PathVariable String treeId, @PathVariable String personId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        List<GeographyResponse> geographiesResponse = personGeographiesViewService.getPersonGeographies(GetPersonParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .personId(personId)
                .build());
        return ResponseEntity.ok(geographiesResponse);
    }
}
