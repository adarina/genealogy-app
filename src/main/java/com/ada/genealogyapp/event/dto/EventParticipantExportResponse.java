package com.ada.genealogyapp.event.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventParticipantExportResponse {

    private String participantId;

    private String relationship;
}
