package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveLocationFromEventParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventLocationManagementService {

    private final EventService eventService;


    @TransactionalInNeo4j
    public void removeLocationFromEvent(RemoveLocationFromEventParams params) {
        eventService.removeLocationFromEvent(params);
    }

    @TransactionalInNeo4j
    public void addLocationToEvent(AddLocationToEventParams params) {
        eventService.addLocationToEvent(params);
    }

}
