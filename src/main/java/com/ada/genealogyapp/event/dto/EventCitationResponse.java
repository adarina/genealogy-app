package com.ada.genealogyapp.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventCitationResponse {

    private String id;

    private String page;

    private String date;

    private String name;
}