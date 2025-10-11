package com.ada.genealogyapp.location.controller;


import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.location.dto.LocationResponse;
import com.ada.genealogyapp.location.dto.LocationWithParentsResponse;
import com.ada.genealogyapp.location.dto.params.GetLocationParams;
import com.ada.genealogyapp.location.service.LocationParentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations/{locationId}")
public class LocationParentViewController {

    private final LocationParentViewService locationParentViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping("/parent")
    public ResponseEntity<LocationResponse> getLocationParent(@PathVariable String treeId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LocationResponse locationResponses = locationParentViewService.getLocationParent(GetLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("/parents")
    public ResponseEntity<LocationWithParentsResponse> getLocationWithParents(@PathVariable String treeId, @PathVariable String locationId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        LocationWithParentsResponse locationResponse = locationParentViewService.getLocationParents(GetLocationParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.ok(locationResponse);
    }
}
