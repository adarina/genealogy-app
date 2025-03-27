package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("api/v1/genealogy/types/eventParticipantRelationship")
public class EventParticipantRelationshipTypeController {

    @GetMapping
    public List<EventParticipantRelationshipType> getEventParticipantRelationshipTypes() {
        return Arrays.asList(EventParticipantRelationshipType.values());
    }
}
