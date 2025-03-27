package com.ada.genealogyapp.person.controller;


import com.ada.genealogyapp.person.dto.PersonFamilyResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonFamiliesParams;
import com.ada.genealogyapp.person.service.PersonFamiliesViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/families")
public class PersonFamiliesViewController {

    private final PersonFamiliesViewService personFamiliesViewService;

    @GetMapping
    public ResponseEntity<Page<PersonFamilyResponse>> getPersonalFamilies(@PathVariable String treeId, @PathVariable String personId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<PersonFamilyResponse> personFamiliesResponses = personFamiliesViewService.getPersonalFamilies(GetPersonFamiliesParams.builder()
                .userId(userId)
                .treeId(treeId)
                .personId(personId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(personFamiliesResponses);
    }
}