package com.ada.genealogyapp.event.dto;


import com.ada.genealogyapp.event.type.EventType;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;



@Data
@SuperBuilder
@NoArgsConstructor
public class EventRequest {

    private EventType type;

    private String date;

    private String place;

    private String description;

}