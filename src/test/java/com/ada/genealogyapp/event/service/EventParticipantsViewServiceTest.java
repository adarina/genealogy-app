package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventParticipantResponse;
import com.ada.genealogyapp.event.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.ada.genealogyapp.tree.service.TreeService;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.util.Collections;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class EventParticipantsViewServiceTest {

    @Mock
    TreeService treeService;

    @Mock
    EventService eventService;

    @Mock
    EventRepository eventRepository;

    @InjectMocks
    EventParticipantsViewService eventParticipantsViewService;

    String treeId;
    String eventId;
    Page<EventParticipantResponse> mockedPage;
    Pageable pageable;

    @BeforeEach
    void setUp() {

        treeId = String.valueOf(UUID.randomUUID());
        eventId = String.valueOf(UUID.randomUUID());
        pageable = PageRequest.of(0, 10);
        mockedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
    }

    @Test
    void getEventParticipants_shouldReturnParticipantsWhenTreeAndEventExist() {

        doNothing().when(treeService).ensureTreeExists(treeId);
        doNothing().when(eventService).ensureEventExists(eventId);
        when(eventRepository.findEventParticipants(treeId, eventId, pageable)).thenReturn(mockedPage);

        Page<EventParticipantResponse> result = eventParticipantsViewService.getEventParticipants(treeId, eventId, pageable);

        assertEquals(mockedPage, result);
        verify(treeService).ensureTreeExists(treeId);
        verify(eventService).ensureEventExists(eventId);
        verify(eventRepository).findEventParticipants(treeId, eventId, pageable);
    }
}
