package com.ada.genealogyapp.event.dto.params;


import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateEventRequestParams extends BaseEventParams {
    private EventRequest eventRequest;
    private Event event;
}