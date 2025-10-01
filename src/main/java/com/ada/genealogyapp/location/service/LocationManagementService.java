package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.exceptions.ValidationException;
import com.ada.genealogyapp.location.dto.params.*;
import com.ada.genealogyapp.location.model.Location;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationManagementService {

    private final LocationService locationService;

    public Location buildAndValidateLocation(UpdateLocationRequestParams params) throws ValidationException {
        Location location = Location.builder()
                .name(params.getLocationRequest().getName())
                .type(params.getLocationRequest().getType())
                .isMain(params.getLocationRequest().getIsMain())
                .latitude(params.getLocationRequest().getLatitude())
                .longitude(params.getLocationRequest().getLongitude())
                .build();
        //TODO locationValidationService.validateLocation(location);
        return location;
    }

    @TransactionalInNeo4j
    public void updateLocation(UpdateLocationRequestParams params) {
        Location location = buildAndValidateLocation(params);
        locationService.updateLocation(UpdateLocationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .locationId(params.getLocationId())
                .location(location)
                .build());
    }

    @TransactionalInNeo4j
    public void updateLocationWithParent(UpdateLocationRequestWithParentParams params) throws ValidationException {
        Location location = buildAndValidateLocation(params);
        locationService.updateLocationWithParent(UpdateLocationWithParentParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .locationId(params.getLocationId())
                .location(location)
                .parentId(params.getParentId())
                .build());
    }

    @TransactionalInNeo4j
    public void deleteLocation(DeleteLocationParams params) {
        locationService.deleteLocation(params);
    }
}
