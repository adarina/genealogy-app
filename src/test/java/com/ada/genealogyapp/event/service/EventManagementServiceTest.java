package com.ada.genealogyapp.event.service;

import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.event.dto.params.DeleteEventParams;
import com.ada.genealogyapp.event.dto.params.UpdateEventParams;
import com.ada.genealogyapp.event.dto.params.UpdateEventRequestParams;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;

import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class EventManagementServiceTest {

    @Mock
    EventService eventService;
    @Mock
    EventValidationService eventValidationService;
    @InjectMocks
    EventManagementService eventManagementService;

    @Test
    void updateEvent_shouldInvokeValidationAndService() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";
        String date = "date123";
        String place = "place123";
        String description = "description123";
        EventType type = EventType.BIRTH;

        EventRequest eventRequest = EventRequest.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        UpdateEventRequestParams requestParams = UpdateEventRequestParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .eventRequest(eventRequest)
                .build();

        Event event = Event.builder()
                .date(date)
                .place(place)
                .type(type)
                .description(description)
                .build();

        UpdateEventParams params = UpdateEventParams.builder()
                .userId(requestParams.getUserId())
                .treeId(requestParams.getTreeId())
                .eventId(requestParams.getEventId())
                .event(event)
                .build();

        eventManagementService.updateEvent(requestParams);

        verify(eventValidationService).validateEvent(event);
        verify(eventService).updateEvent(params);
    }

    @Test
    void deleteEvent_shouldInvokeService() {
        String userId = "user123";
        String treeId = "tree123";
        String eventId = "event123";

        DeleteEventParams params = DeleteEventParams.builder()
                .userId(userId)
                .treeId(treeId)
                .eventId(eventId)
                .build();

        eventManagementService.deleteEvent(params);

        verify(eventService).deleteEvent(params);
    }
}