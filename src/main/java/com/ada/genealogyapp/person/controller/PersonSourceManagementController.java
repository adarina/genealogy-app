package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.person.service.PersonSourceManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/sources")
public class PersonSourceManagementController {

    private final PersonSourceManagementService personSourceManagementService;

    private final CitationCreationService citationCreationService;

    public PersonSourceManagementController(PersonSourceManagementService personSourceManagementService, CitationCreationService citationCreationService) {
        this.personSourceManagementService = personSourceManagementService;
        this.citationCreationService = citationCreationService;
    }


    @PostMapping("/addExistingCitation")
    public ResponseEntity<?> addCitationToPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody UUIDRequest UUIDRequest) {
        personSourceManagementService.addCitationToPerson(treeId, personId, UUIDRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/addNewCitation")
    public ResponseEntity<?> createAndAddCitationToPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody CitationRequest citationRequest) {
        Citation citation = citationCreationService.createCitation(treeId, citationRequest);
        personSourceManagementService.addCitationToPerson(treeId, personId, citation.getId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping()
    public ResponseEntity<?> removeCitationFromPerson(@PathVariable UUID treeId, @PathVariable UUID personId, @RequestBody UUIDRequest UUIDRequest) {
        personSourceManagementService.removeCitationFromPerson(treeId, personId, UUIDRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}