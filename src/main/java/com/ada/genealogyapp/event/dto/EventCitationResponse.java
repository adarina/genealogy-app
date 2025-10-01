package com.ada.genealogyapp.event.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventCitationResponse {

    private String id;

    private String page;

    private String date;

    private String name;
}