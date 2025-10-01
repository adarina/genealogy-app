package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventExportResponse {

    private String id;

    private EventType type;

    private String date;

    private String description;

    private String place;

    private String locationId;

    Set<EventParticipantExportResponse> participants;

    Set<EventCitationExportResponse> citations;
}
