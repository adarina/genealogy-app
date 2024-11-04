package com.ada.genealogyapp.event.dto;


import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.person.model.Person;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponse {


    private UUID id;

    private EventType type;

    private LocalDate date;

    private List<Participant> participants;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Participant {

        private UUID id;

        private String name;

    }
}
