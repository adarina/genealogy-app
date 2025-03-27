package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParticipantsParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventParticipantsViewService {

    private final EventRepository eventRepository;

    private final TreeService treeService;

    public Page<EventParticipantResponse> getEventParticipants(GetEventParticipantsParams params) {
        Page<EventParticipantResponse> page = eventRepository.findParticipants(params.getUserId(), params.getTreeId(), params.getEventId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}