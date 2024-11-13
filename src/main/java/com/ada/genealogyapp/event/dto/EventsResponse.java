package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventsResponse {

    private UUID id;

    private String description;

    private LocalDate date;

    private String place;

    private EventType type;

    private String participantNames;

}