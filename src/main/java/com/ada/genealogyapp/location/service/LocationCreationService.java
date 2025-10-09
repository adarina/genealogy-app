package com.ada.genealogyapp.location.service;


import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.location.dto.LocationJsonRequest;
import com.ada.genealogyapp.location.dto.params.*;
import com.ada.genealogyapp.location.model.Location;
import com.ada.genealogyapp.location.repository.LocationRepository;
import com.ada.genealogyapp.location.type.LocationType;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.ada.genealogyapp.gedcom.mappers.LocationMapper.determineType;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.groupingBy;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationCreationService {

    private final LocationRepository locationRepository;

    private final LocationService locationService;

    private final EventService eventService;

//    private final LocationValidationService locationValidationService;

    private Location buildAndValidateLocation(CreateLocationRequestParams params) {
        Location location = Location.builder()
                .id(UUID.randomUUID().toString())
                .name(params.getLocationRequest().getName())
                .type(params.getLocationRequest().getType())
                .latitude(params.getLocationRequest().getLatitude())
                .longitude(params.getLocationRequest().getLongitude())
                .isMain(params.getLocationRequest().getIsMain())
                .build();
//TODO        locationValidationService.validateLocation(location);
        return location;
    }

    private Location buildValidateAndSaveLocation(CreateLocationRequestParams params) {
        Location location = buildAndValidateLocation(params);
        locationService.saveLocation(SaveLocationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .locationId(location.getId())
                .location(location)
                .build());
        return location;
    }

    @TransactionalInNeo4j
    public Location createLocation(CreateLocationRequestParams params) {
        return buildValidateAndSaveLocation(params);
    }


    @TransactionalInNeo4j
    public void createAndAddLocationToEvent(CreateAndAddLocationToEventRequestParams params) {
        Location location = buildValidateAndSaveLocation(params);
        eventService.addLocationToEvent(AddLocationToEventParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .eventId(params.getEventId())
                .locationId(location.getId())
                .build());
    }

    @TransactionalInNeo4j
    public void createAndAddParentToLocation(CreateAndAddParentToLocationRequestParams params) {
        Location parent = buildValidateAndSaveLocation(params);
        locationService.addParentToLocation(AddParentToLocationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .locationId(params.getLocationId())
                .parentId(parent.getId())
                .build());
    }

    @TransactionalInNeo4j
    public Map<String, Location> createLocations(String userId, String treeId, List<LocationJsonRequest> locationRequests) {
        Map<String, Location> createdLocationsMap = new HashMap<>();
        List<Map<String, Object>> locations = new ArrayList<>();

        for (LocationJsonRequest request : locationRequests) {
            Location location = Location.builder()
                    .id(UUID.randomUUID().toString())
                    .name(request.getName())
                    .type(request.getType())
                    .latitude(request.getLatitude())
                    .longitude(request.getLongitude())
                    .isMain(request.getIsMain())
                    .build();
//            locationValidationService.validateLocation(location);

            Map<String, Object> locationData = new HashMap<>();
            locationData.put("id", location.getId());
            locationData.put("name", location.getName());
            locationData.put("type", location.getType().name());
            locationData.put("latitude", location.getLatitude());
            locationData.put("longitude", location.getLongitude());
            locationData.put("isMain", location.getIsMain());


            locations.add(locationData);
            createdLocationsMap.put(request.getId(), location);
        }
        locationService.saveLocations(userId, treeId, locations);
        return createdLocationsMap;
    }

    public Location createLocationAndHierarchy(CreateLocationAndHierarchyParams params) {
        if (isNull(params.getHierarchy())) {
            return null;
        }
        String trimmedHierarchy = params.getHierarchy().trim();
        if (trimmedHierarchy.isEmpty()) {
            return null;
        }

        String[] parts = trimmedHierarchy.split(",");
        Location parent = null;
        Location firstLocation = null;

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (isNull(part)) {
                continue;
            }
            String partName = part.trim();
            if (partName.isEmpty()) {
                continue;
            }
            LocationType type = determineType(partName, params.getAddress().getCountry(), params.getAddress().getState(), params.getAddress().getCity());
            boolean isMain = (i == parts.length - 1);

            Location currentLocation;

            if (parent == null) {
                currentLocation = locationRepository.findOrCreateTopLevelLocation(
                        params.getTreeId(),
                        partName,
                        String.valueOf(type),
                        isMain,
                        params.getLatitude(),
                        params.getLongitude()
                );
            } else {
                currentLocation = locationRepository.findOrCreateChildLocation(
                        params.getTreeId(),
                        parent.getId(),
                        partName,
                        String.valueOf(type),
                        isMain
                );
            }
            if (firstLocation == null) {
                firstLocation = currentLocation;
            }
            parent = currentLocation;
        }
        return firstLocation;
    }
}
