package com.ada.genealogyapp.person.dto;

import com.ada.genealogyapp.event.type.EventRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import lombok.*;

import java.time.LocalDate;
import java.util.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonEventsResponse {


    private UUID id;

    private EventRelationshipType relationship;

    private EventType type;

    private String description;

    private LocalDate date;

    private String place;



    private List<Participant> participants;


    private List<Citation> citations;

    @Getter
    @AllArgsConstructor
    public static class Participant {

        private UUID id;

        private String name;

    }

    @Getter
    @AllArgsConstructor
    public static class Citation {

        private UUID id;

    }
}