package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.params.*;

import java.util.List;
import java.util.Map;


public interface LocationService {
    void saveLocation(SaveLocationParams params);

    void updateLocation(UpdateLocationParams params);

    void addParentToLocation(AddParentToLocationParams params);

    void removeParentFromLocation(RemoveParentFromLocationParams params);

    void updateLocationWithParent(UpdateLocationWithParentParams params);

    void deleteLocation(DeleteLocationParams params);

    void saveLocations(String userId, String treeId, List<Map<String, Object>> locationsData);

    void addLocatedInRelationships(String userId, String id, List<Map<String, Object>> relationshipsData);
}
