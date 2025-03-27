package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.event.dto.*;
import com.ada.genealogyapp.event.dto.params.GetEventParams;
import com.ada.genealogyapp.event.dto.params.GetEventsParams;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ada.genealogyapp.event.repository.EventRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import com.ada.genealogyapp.tree.service.TreeService;
import org.mockito.InjectMocks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;


@ExtendWith(MockitoExtension.class)
class EventViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    EventRepository eventRepository;
    @Mock
    ObjectMapper objectMapper;
    @InjectMocks
    EventViewService eventViewService;

    @Test
    void getEvents_shouldReturnEventsWhenValidFilter() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"description\":\"Birth of John Smith\",\"participants\":\"John Smith\",\"type\":\"BIRTH\",\"place\":\"Poland\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventsParams params = GetEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        EventFilterRequest filterRequest = EventFilterRequest.builder()
                .description("Birth of John Smith")
                .participants("John Smith")
                .type("BIRTH")
                .place("Poland")
                .build();

        Page<EventsResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(objectMapper.readValue(filter, EventFilterRequest.class)).thenReturn(filterRequest);
        when(eventRepository.find(userId, treeId, "Birth of John Smith", "John Smith", "BIRTH", "Poland", pageable))
                .thenReturn(expectedPage);

        Page<EventsResponse> result = eventViewService.getEvents(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);

        verify(objectMapper).readValue(eq(filter), eq(EventFilterRequest.class));
        verify(treeService).ensureUserAndTreeExist(any(GetEventsParams.class), eq(expectedPage));

    }

    @Test
    void getEvents_shouldThrowExceptionWhenUserDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"description\":\"Birth of John Smith\",\"participants\":\"John Smith\",\"type\":\"BIRTH\",\"place\":\"Poland\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventsParams params = GetEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        EventFilterRequest filterRequest = EventFilterRequest.builder()
                .description("Birth of John Smith")
                .participants("John Smith")
                .type("BIRTH")
                .place("Poland")
                .build();

        when(objectMapper.readValue(filter, EventFilterRequest.class)).thenReturn(filterRequest);
        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventViewService.getEvents(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }


    @Test
    void getEvents_shouldThrowExceptionWhenTreeDoesNotExist() throws JsonProcessingException {
        String userId = "user123";
        String treeId = "tree123";
        String filter = "{\"description\":\"Birth of John Smith\",\"participants\":\"John Smith\",\"type\":\"BIRTH\",\"place\":\"Poland\"}";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventsParams params = GetEventsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .filter(filter)
                .pageable(pageable)
                .build();

        EventFilterRequest filterRequest = EventFilterRequest.builder()
                .description("Birth of John Smith")
                .participants("John Smith")
                .type("BIRTH")
                .place("Poland")
                .build();

        when(objectMapper.readValue(filter, EventFilterRequest.class)).thenReturn(filterRequest);

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventViewService.getEvents(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getEvent_shouldReturnEventWhenExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        GetEventParams params = GetEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        EventResponse expectedEvent = EventResponse.builder()
                .id(eventId)
                .build();

        when(eventRepository.find(userId, treeId, eventId))
                .thenReturn(expectedEvent);

        EventResponse result = eventViewService.getEvent(params);

        assertNotNull(result);
        assertEquals(expectedEvent, result);
        verify(treeService).ensureUserAndTreeExist(any(GetEventParams.class), eq(expectedEvent));
    }

    @Test
    void getEvent_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        GetEventParams params = GetEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventViewService.getEvent(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getEvent_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        GetEventParams params = GetEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        doThrow(new NodeNotFoundException("User not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventViewService.getEvent(params));

        assertEquals("User not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}