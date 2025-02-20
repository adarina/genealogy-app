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
    public void removeCitationFromEvent(String userId, String treeId, String eventId, String citationId) {
        eventService.removeCitationFromEvent(userId, treeId, eventId, citationId);
    }

    @TransactionalInNeo4j
    public void addCitationToEvent(String userId, String treeId, String eventId, String citationId) {
        eventService.addCitationToEvent(userId, treeId, eventId, citationId);
    }
}
