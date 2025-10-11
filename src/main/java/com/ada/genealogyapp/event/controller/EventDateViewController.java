package com.ada.genealogyapp.event.controller;

import com.ada.genealogyapp.authentication.IAuthenticationFacade;
import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.service.EventDateViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/genealogy/trees/{treeId}/events/{eventId}/date")
public class EventDateViewController {

    private final EventDateViewService eventDateViewService;

    private final IAuthenticationFacade authenticationFacade;

    @GetMapping
    public ResponseEntity<Date> getEventDate(@PathVariable String treeId, @PathVariable String eventId) {
        Authentication authentication = authenticationFacade.getAuthentication();
        Date eventDateResponse = eventDateViewService.getEventDate(GetEventParams.builder()
                .userId(authentication.getName())
                .treeId(treeId)
                .eventId(eventId)
                .build());
        return ResponseEntity.ok(eventDateResponse);
    }
}
