package com.ada.genealogyapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EventFilterRequest {

    private String description;

    private String participants;

    private String type;
}
