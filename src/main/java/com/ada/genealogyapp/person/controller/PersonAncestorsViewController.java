package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.person.dto.PersonAncestorResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.service.PersonAncestorsViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/ancestors")
public class PersonAncestorsViewController {

    private final PersonAncestorsViewService personAncestorsViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<PersonAncestorResponse> getPersonAncestors(@PathVariable String treeId, @PathVariable String personId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        PersonAncestorResponse ancestorsResponse = personAncestorsViewService.getPersonAncestors(GetPersonParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .personId(personId)
                .build());
        return ResponseEntity.ok(ancestorsResponse);
    }
}
