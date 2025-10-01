package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.location.dto.LocationResponse;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class EventLocationViewService {

    private final EventRepository eventRepository;

    private final TreeService treeService;

    public LinkedHashSet<LocationResponse> getEventLocation(GetEventParams params) {
        LinkedHashSet<LocationResponse> list = eventRepository.findLocation(params.getUserId(), params.getTreeId(), params.getEventId());
        treeService.ensureUserAndTreeExist(params, list);
        return list;
    }
}