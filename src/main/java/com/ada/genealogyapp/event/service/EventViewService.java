package com.ada.genealogyapp.event.service;


import com.ada.genealogyapp.event.dto.EventFilterRequest;
import com.ada.genealogyapp.event.dto.EventResponse;
import com.ada.genealogyapp.event.dto.EventsResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.dto.params.GetEventsParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;



@Service
@Slf4j
@RequiredArgsConstructor
public class EventViewService {

    private final EventRepository eventRepository;

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    public Page<EventsResponse> getEvents(GetEventsParams params) throws JsonProcessingException {
        EventFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), EventFilterRequest.class);
        Page<EventsResponse> page = eventRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getDescription(), filterRequest.getParticipants(), filterRequest.getType(), filterRequest.getPlace(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public EventResponse getEvent(GetEventParams params) {
        EventResponse eventResponse = eventRepository.find(params.getUserId(), params.getTreeId(), params.getEventId());
        treeService.ensureUserAndTreeExist(params, eventResponse);
        return eventResponse;
    }
}