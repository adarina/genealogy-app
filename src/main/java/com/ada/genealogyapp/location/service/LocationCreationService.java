package com.ada.genealogyapp.location.service;


import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.service.EventService;
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
import static java.util.Objects.nonNull;
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

    //TODO coś nie tak bo kościoły i cmentarze się zagubiły

    public Location createLocationAndHierarchy(CreateLocationAndHierarchyParams params) {
        if (isNull(params.getHierarchy())) {
            return null;
        }
        String trimmedHierarchy = params.getHierarchy().trim();
        if (trimmedHierarchy.isEmpty()) {
            return null;
        }
        Map<String, List<Location>> existingLocations = locationRepository.findAllByTreeId(params.getTreeId()).stream()
                .collect(groupingBy(Location::getName));

        Location parent = null;
        Location firstLocation = null;

        String[] parts = trimmedHierarchy.split(",");

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

            List<Location> locationsWithSameName = existingLocations.getOrDefault(partName, new ArrayList<>());
            Location currentLocation = locationsWithSameName.stream()
                    .filter(location -> location.getType() == type)
                    .findFirst()
                    .orElse(null);

            if (isNull(currentLocation)) {
                currentLocation = Location.builder()
                        .id(UUID.randomUUID().toString())
                        .type(type)
                        .name(partName)
                        .latitude(null)
                        .longitude(null)
                        .isMain(i == parts.length - 1)
                        .build();

                if (isNull(firstLocation)) {
                    if (nonNull(params.getLatitude())) currentLocation.setLatitude(params.getLatitude());
                    if (nonNull(params.getLongitude())) currentLocation.setLongitude(params.getLongitude());
                }
                locationService.saveLocation(SaveLocationParams.builder()
                        .userId(params.getUserId())
                        .treeId(params.getTreeId())
                        .locationId(currentLocation.getId())
                        .location(currentLocation)
                        .build());
                existingLocations.computeIfAbsent(partName, k -> new ArrayList<>()).add(currentLocation);
            }
            if (isNull(firstLocation)) {
                firstLocation = currentLocation;
            }
            if (nonNull(parent)) {
                locationRepository.createLocatedInRelationship(parent.getId(), currentLocation.getId());
            }
            parent = currentLocation;
        }
        return firstLocation;
    }
}
