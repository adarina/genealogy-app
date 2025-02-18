package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCitationManagementService {

    private final EventService eventService;


    @TransactionalInNeo4j
    public void removeCitationFromEvent(String treeId, String eventId, String citationId) {
        eventService.removeCitationFromEvent(treeId, eventId, citationId);
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(String treeId, String eventId, String citationId) {
        eventService.addCitationToEvent(treeId, eventId, citationId);
    }
}
