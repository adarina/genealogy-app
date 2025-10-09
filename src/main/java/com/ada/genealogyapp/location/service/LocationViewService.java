package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.*;
import com.ada.genealogyapp.location.dto.params.GetLocationParams;
import com.ada.genealogyapp.location.repository.LocationRepository;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationViewService {

    private final LocationRepository locationRepository;

    private final TreeService treeService;


    public LocationResponse getLocation(GetLocationParams params) {
        LocationResponse locationResponse = locationRepository.find(params.getUserId(), params.getTreeId(), params.getLocationId());
        treeService.ensureUserAndTreeExist(params, locationResponse);
        return locationResponse;
    }

    public List<LocationChildResponse> getLocationsParents(GetLocationParams params) {
        List<LocationResponse> locations = locationRepository.findMain(params.getUserId(), params.getTreeId());

        Map<LocationResponse, Set<LocationResponse>> childrenMap = new HashMap<>();
        Set<String> visited = new HashSet<>();

        for (LocationResponse location : locations) {
            buildChildrenMapRecursive(location, childrenMap, visited, params);
        }
        return mapToListResponse(locations, childrenMap);
    }

    private void buildChildrenMapRecursive(LocationResponse location, Map<LocationResponse, Set<LocationResponse>> children, Set<String> visited, GetLocationParams params) {
        if (isNull(location)|| visited.contains(location.getId())) {
            return;
        }
        visited.add(location.getId());

        Set<LocationResponse> childLocations = locationRepository.findLocationsIn(params.getUserId(), params.getTreeId(), location.getId());
        if (isNull(childLocations) || childLocations.isEmpty()) {
            return;
        }

        children.put(location, childLocations);

        for (LocationResponse child : childLocations) {
            if (child != null && !visited.contains(child.getId())) {
                buildChildrenMapRecursive(child, children, visited, params);
            }
        }
    }

    private List<LocationChildResponse> mapToListResponse(List<LocationResponse> mainLocations, Map<LocationResponse, Set<LocationResponse>> locationMap) {
        return mainLocations.stream()
                .filter(Objects::nonNull)
                .map(location -> buildResponse(location, locationMap, new HashSet<>()))
                .filter(Objects::nonNull)
                .toList();
    }

    private LocationChildResponse buildResponse(LocationResponse location, Map<LocationResponse, Set<LocationResponse>> locationMap, Set<String> visited) {
        if (location == null || visited.contains(location.getId())) return null;

        visited.add(location.getId());

        List<LocationChildResponse> children = ofNullable(locationMap.get(location))
                .orElse(Collections.emptySet())
                .stream()
                .filter(Objects::nonNull)
                .map(child -> buildResponse(child, locationMap, visited))
                .parallel()
                .filter(Objects::nonNull)
                .toList();

        return LocationChildResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .type(location.getType())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .children(children)
                .build();
    }
    public Set<LocationExportResponse> findLocations(BaseParams params) {
        return locationRepository.find(params.getUserId(), params.getTreeId());
    }
}
