package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.date.model.Date;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.ada.genealogyapp.date.service.DateExtractor.parseDate;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventDateViewService {

    private final EventRepository eventRepository;

    private final TreeService treeService;


    public Date getEventDate(GetEventParams params) {
        EventResponse eventResponse = eventRepository.find(params.getUserId(), params.getTreeId(), params.getEventId());
        treeService.ensureUserAndTreeExist(params, eventResponse);
        return parseDate(eventResponse.getDate());
    }
}
