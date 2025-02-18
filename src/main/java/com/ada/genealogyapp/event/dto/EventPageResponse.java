package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.type.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventPageResponse {

    private String id;

    private String description;

    private String date;

    private String place;

    private EventType type;

    private String participantNames;

}