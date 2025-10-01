package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.location.dto.GeographyResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.service.PersonGeographyViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/geographies")
public class PersonGeographiesViewController {

    private final PersonGeographyViewService personGeographiesViewService;

    @GetMapping
    public ResponseEntity<List<GeographyResponse>> getPersonGeographies(@PathVariable String treeId, @PathVariable String personId, @RequestHeader(value = "X-User-Id") String userId) {
        List<GeographyResponse> geographiesResponse = personGeographiesViewService.getPersonGeographies(GetPersonParams.builder()
                .userId(userId)
                .treeId(treeId)
                .personId(personId)
                .build());
        return ResponseEntity.ok(geographiesResponse);
    }
}
