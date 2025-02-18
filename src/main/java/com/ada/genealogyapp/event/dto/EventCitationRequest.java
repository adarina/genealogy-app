package com.ada.genealogyapp.event.dto;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventCitationRequest {

    private Long id;

    private String citationId;
}
