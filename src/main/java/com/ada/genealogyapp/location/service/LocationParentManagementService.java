package com.ada.genealogyapp.location.service;

import com.ada.genealogyapp.location.dto.params.RemoveParentFromLocationParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationParentManagementService {

    private final LocationService locationService;

    @TransactionalInNeo4j
    public void removeParentFromLocation(RemoveParentFromLocationParams params) {
        locationService.removeParentFromLocation(params);
    }
}
