package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.dto.params.RemoveCitationFromEventParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCitationManagementService {

    private final EventService eventService;


    @TransactionalInNeo4j
    public void removeCitationFromEvent(RemoveCitationFromEventParams params) {
        eventService.removeCitationFromEvent(params);
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(AddCitationToEventParams params) {
        eventService.addCitationToEvent(params);
    }
}
