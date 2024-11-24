package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.person.dto.PersonAncestorResponse;
import com.ada.genealogyapp.person.service.PersonAncestorsViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/ancestors")
public class PersonAncestorsViewController {

    private final PersonAncestorsViewService personAncestorsViewService;

    public PersonAncestorsViewController(PersonAncestorsViewService personAncestorsViewService) {
        this.personAncestorsViewService = personAncestorsViewService;
    }

    @GetMapping
    public ResponseEntity<PersonAncestorResponse> getPersonAncestors(@PathVariable UUID treeId, @PathVariable UUID personId) {
        PersonAncestorResponse ancestorsResponse = personAncestorsViewService.getPersonAncestors(treeId, personId);
        return ResponseEntity.ok(ancestorsResponse);
    }
}
