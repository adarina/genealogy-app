package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.params.*;
import com.ada.genealogyapp.location.repository.LocationRepository;
import com.ada.genealogyapp.query.IdType;
import com.ada.genealogyapp.query.QueryResultProcessor;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class LocationDataManager implements LocationService {

    private final LocationRepository locationRepository;

    private final QueryResultProcessor processor;

    public void saveLocation(SaveLocationParams params) {
        String result = locationRepository.save(params.getUserId(), params.getTreeId(), params.getLocationId(), params.getLocation().getName(), params.getLocation().getType().name(), params.getLocation().getIsMain(), params.getLocation().getLatitude(), params.getLocation().getLongitude());
        processor.process(result, Map.of(IdType.TREE_ID, params.getTreeId(), IdType.LOCATION_ID, params.getLocationId()));
    }

    public void updateLocation(UpdateLocationParams params) {
        String result = locationRepository.update(params.getUserId(), params.getTreeId(), params.getLocationId(), params.getLocation().getName(), params.getLocation().getType().name(), params.getLocation().getLatitude(), params.getLocation().getLongitude());
        processor.process(result, Map.of(IdType.LOCATION_ID, params.getLocationId()));
    }

    public void deleteLocation(DeleteLocationParams params) {
        String result = locationRepository.delete(params.getUserId(), params.getTreeId(), params.getLocationId());
        processor.process(result, Map.of(IdType.LOCATION_ID, params.getLocationId()));
    }

    public void addParentToLocation(AddParentToLocationParams params) {
        String result = locationRepository.addParent(params.getUserId(), params.getTreeId(), params.getLocationId(), params.getParentId());
        processor.process(result, Map.of(IdType.TREE_ID, params.getLocationId(), IdType.PARENT_ID, params.getParentId()));
    }

    public void removeParentFromLocation(RemoveParentFromLocationParams params) {
        String result = locationRepository.removeParent(params.getUserId(), params.getTreeId(), params.getLocationId(), params.getParentId());
        processor.process(result, Map.of(IdType.LOCATION_ID, params.getLocationId(), IdType.PARENT_ID, params.getParentId()));
    }


    public void updateLocationWithParent(UpdateLocationWithParentParams params) {
        String result = locationRepository.update(params.getUserId(), params.getTreeId(), params.getLocationId(), params.getLocation().getName(),  params.getLocation().getType().name(), params.getLocation().getLatitude(), params.getLocation().getLongitude(), params.getParentId());
        processor.process(result, Map.of(IdType.PARENT_ID, params.getParentId(), IdType.LOCATION_ID, params.getLocationId()));
    }

    public void saveLocations(String userId, String treeId, List<Map<String, Object>> locationsData) {
        locationRepository.saveLocations(userId, treeId, locationsData);
    }

    @TransactionalInNeo4j
    public void addLocatedInRelationships(String userId, String treeId, List<Map<String, Object>> relationshipsData) {
        locationRepository.addLocatedInRelationships(userId, treeId, relationshipsData);
    }
}
