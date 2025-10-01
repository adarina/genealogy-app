package com.ada.genealogyapp.location.controller;


import com.ada.genealogyapp.location.dto.LocationResponse;
import com.ada.genealogyapp.location.dto.LocationWithParentsResponse;
import com.ada.genealogyapp.location.dto.params.GetLocationParams;
import com.ada.genealogyapp.location.service.LocationParentViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/locations/{locationId}")
public class LocationParentViewController {

    private final LocationParentViewService locationParentViewService;

    @GetMapping("/parent")
    public ResponseEntity<LocationResponse> getLocationParent(@PathVariable String treeId, @PathVariable String locationId, @RequestHeader(value = "X-User-Id") String userId) {
        LocationResponse locationResponses = locationParentViewService.getLocationParent(GetLocationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.ok(locationResponses);
    }

    @GetMapping("/parents")
    public ResponseEntity<LocationWithParentsResponse> getLocationWithParents(@PathVariable String treeId, @PathVariable String locationId, @RequestHeader(value = "X-User-Id") String userId) {
        LocationWithParentsResponse locationResponse = locationParentViewService.getLocationParents(GetLocationParams.builder()
                .userId(userId)
                .treeId(treeId)
                .locationId(locationId)
                .build());
        return ResponseEntity.ok(locationResponse);
    }
}
