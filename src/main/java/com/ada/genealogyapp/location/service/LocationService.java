package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.params.*;



public interface LocationService {
    void saveLocation(SaveLocationParams params);

    void updateLocation(UpdateLocationParams params);

    void addParentToLocation(AddParentToLocationParams params);

    void removeParentFromLocation(RemoveParentFromLocationParams params);

    void updateLocationWithParent(UpdateLocationWithParentParams params);

    void deleteLocation(DeleteLocationParams params);

}
