package com.ada.genealogyapp.person.controller;

import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.service.ParticipantEventsViewService;
import com.ada.genealogyapp.person.dto.params.GetParticipantEventsParams;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/persons/{personId}/events")
public class PersonEventsViewController {

    private final ParticipantEventsViewService participantEventsViewService;

    @GetMapping
    public ResponseEntity<Page<ParticipantEventResponse>> getPersonEvents(@PathVariable String treeId, @PathVariable String personId, @PageableDefault Pageable pageable, @RequestHeader(value = "X-User-Id") String userId) {
        Page<ParticipantEventResponse> eventResponses = participantEventsViewService.getParticipantEvents(GetParticipantEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .participantId(personId)
                .pageable(pageable)
                .build());
        return ResponseEntity.ok(eventResponses);
    }
}
