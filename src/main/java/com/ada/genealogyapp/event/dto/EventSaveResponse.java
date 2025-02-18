package com.ada.genealogyapp.event.dto;

import com.ada.genealogyapp.event.model.Event;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventSaveResponse {
    private String message;
    private Event event;
}
