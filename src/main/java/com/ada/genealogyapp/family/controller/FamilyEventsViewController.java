package com.ada.genealogyapp.family.controller;


import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.service.ParticipantEventsViewService;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventParams;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventsParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/families/{familyId}/events")
public class FamilyEventsViewController {

    private final ParticipantEventsViewService participantEventsViewService;

    @GetMapping
    public ResponseEntity<Page<ParticipantEventResponse>> getFamilyEvents(@PathVariable String treeId, @PathVariable String familyId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<ParticipantEventResponse> eventResponses = participantEventsViewService.getParticipantEvents(GetParticipantEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(familyId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(eventResponses);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<ParticipantEventResponse> getFamilyEvent(@PathVariable String treeId, @PathVariable String familyId, @PathVariable String eventId, @RequestHeader(value = "X-User-Id") String userId) {
        ParticipantEventResponse participantEventResponse = participantEventsViewService.getParticipantEvent(GetParticipantEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(familyId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(participantEventResponse);
    }
}