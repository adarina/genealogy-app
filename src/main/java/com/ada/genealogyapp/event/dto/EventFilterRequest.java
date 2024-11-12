package com.ada.genealogyapp.event.dto;

import lombok.Getter;

@Getter
public class EventFilterRequest {

    private String description;

    private String participants;

    private String type;
}
