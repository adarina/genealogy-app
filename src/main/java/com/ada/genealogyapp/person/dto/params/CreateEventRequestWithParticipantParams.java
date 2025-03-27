package com.ada.genealogyapp.person.dto.params;


import com.ada.genealogyapp.event.dto.params.CreateEventRequestParams;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateEventRequestWithParticipantParams extends CreateEventRequestParams {
    private ParticipantEventRequest participantEventRequest;
    private String participantId;
    private String relationshipType;
}
