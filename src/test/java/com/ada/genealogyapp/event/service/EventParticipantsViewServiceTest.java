package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.dto.params.GetEventParticipantsParams;
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
class EventParticipantsViewServiceTest {

    @Mock
    TreeService treeService;
    @Mock
    EventRepository eventRepository;
    @InjectMocks
    EventParticipantsViewService eventParticipantsViewService;

    @Test
    void getEventParticipants_shouldReturnEventParticipantsWhenValidFilter() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventParticipantsParams params = GetEventParticipantsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();

        Page<EventParticipantResponse> expectedPage = new PageImpl<>(new ArrayList<>());

        when(eventRepository.findParticipants(userId, treeId, eventId, pageable))
                .thenReturn(expectedPage);

        Page<EventParticipantResponse> result = eventParticipantsViewService.getEventParticipants(params);

        assertNotNull(result);
        assertEquals(expectedPage, result);
        verify(treeService).ensureUserAndTreeExist(any(GetEventParticipantsParams.class), eq(expectedPage));
    }

    @Test
    void getEventParticipants_shouldThrowExceptionWhenUserDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventParticipantsParams params = GetEventParticipantsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();


        doThrow(new NodeNotFoundException("User not exist with ID: " + userId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventParticipantsViewService.getEventParticipants(params));

        assertEquals("User not exist with ID: " + userId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }

    @Test
    void getEventParticipants_shouldThrowExceptionWhenTreeDoesNotExist() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "eventId";
        Pageable pageable = PageRequest.of(0, 10);

        GetEventParticipantsParams params = GetEventParticipantsParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .pageable(pageable)
                .build();

        doThrow(new NodeNotFoundException("Tree not exist with ID: " + treeId))
                .when(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());

        NodeNotFoundException exception = assertThrows(NodeNotFoundException.class, () -> eventParticipantsViewService.getEventParticipants(params));

        assertEquals("Tree not exist with ID: " + treeId, exception.getMessage());
        verify(treeService).ensureUserAndTreeExist(any(BaseParams.class), any());
    }
}