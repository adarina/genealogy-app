package com.ada.genealogyapp.event.dto.params;

import com.ada.genealogyapp.event.model.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SaveEventWithParticipantParams extends BaseEventParams {
    private Event event;
    private String participantId;
    private String relationshipType;
}