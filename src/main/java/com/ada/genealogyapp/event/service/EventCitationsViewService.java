package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.params.GetEventCitationsParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventCitationsViewService {

    private final EventRepository eventRepository;

    private final TreeService treeService;

    public Page<EventCitationResponse> getEventCitations(GetEventCitationsParams params) {
        Page<EventCitationResponse> page = eventRepository.findCitations(params.getUserId(), params.getTreeId(), params.getEventId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}
