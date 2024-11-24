package com.ada.genealogyapp.event.dto;


import com.ada.genealogyapp.event.type.EventType;

import lombok.*;

import java.time.LocalDate;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    private EventType type;

    private LocalDate date;

    private String place;

    private String description;

}