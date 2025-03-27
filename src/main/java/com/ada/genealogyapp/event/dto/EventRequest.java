package com.ada.genealogyapp.event.dto;


import com.ada.genealogyapp.event.type.EventType;

import lombok.*;
import lombok.experimental.SuperBuilder;




@Data
@SuperBuilder
@NoArgsConstructor
public class EventRequest {

    private EventType type;

    private String date;

    private String place;

    private String description;

}