package com.ada.genealogyapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class EventFilterRequest {

    private String description;

    private String participants;

    private String type;

    private String place;
}
