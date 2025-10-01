package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.LocationResponse;
import com.ada.genealogyapp.location.dto.LocationWithParentsResponse;
import com.ada.genealogyapp.location.dto.params.GetLocationParams;
import com.ada.genealogyapp.location.repository.LocationRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class LocationParentViewService {

    private final LocationRepository locationRepository;

    private final TreeService treeService;

    public LocationResponse getLocationParent(GetLocationParams params) {
        LocationResponse locationResponse = locationRepository.findParent(params.getUserId(), params.getTreeId(), params.getLocationId());
        treeService.ensureUserAndTreeExist(params, locationResponse);
        return locationResponse;
    }

    public LocationWithParentsResponse getLocationParents(GetLocationParams params) {
        LocationWithParentsResponse locationResponse = locationRepository.findParents(params.getUserId(), params.getTreeId(), params.getLocationId());
        treeService.ensureUserAndTreeExist(params, locationResponse);
        return locationResponse;
    }
}
