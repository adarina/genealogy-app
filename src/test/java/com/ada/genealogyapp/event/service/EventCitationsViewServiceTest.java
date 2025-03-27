package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.params.GetEventCitationsParams;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.ada.genealogyapp.event.repository.EventRepository;
import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.tree.service.TreeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventCitationsViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    EventCitationsViewService eventCitationsViewService;

    @Test
    void getEventCitations_shouldReturnEventCitationsWhenValidFilter() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventCitationsParams params = GetEventCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();

        Page<EventCitationResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(eventRepository.findCitations(userId, treeId, eventId, pageable))
                .thenReturn(expectedPage);

        Page<EventCitationResponse> result = eventCitationsViewService.getEventCitations(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(treeService).ensureUserAndTreeExist(any(GetEventCitationsParams.class), eq(expectedPage));
    }

    @Test
    void getEventCitations_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventCitationsParams params = GetEventCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();


        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventCitationsViewService.getEventCitations(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getEventCitations_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventCitationsParams params = GetEventCitationsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventCitationsViewService.getEventCitations(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}