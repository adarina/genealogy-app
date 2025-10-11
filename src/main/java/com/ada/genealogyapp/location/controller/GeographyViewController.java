package com.ada.genealogyapp.location.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.GeographyResponse;
import com.ada.genealogyapp.location.dto.params.GetLocationsParams;
import com.ada.genealogyapp.location.service.GeographyViewService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/geographies")
public class GeographyViewController {

    private final GeographyViewService geographyViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<List<GeographyResponse>> getGeographies(@PathVariable String treeId, @RequestParam String filter) throws JsonProcessingException {
        Authentication authentication = authenticationFacade.getAuthentication();
        List<GeographyResponse> locationResponses = geographyViewService.getGeographies(GetLocationsParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .filter(filter)
                .build());
        return ResponseEntity.ok(locationResponses);
    }
}
