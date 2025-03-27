package com.ada.genealogyapp.event.dto.params;

import com.ada.genealogyapp.event.dto.params.UpdateEventRequestParams;
import com.ada.genealogyapp.participant.dto.BaseParticipantParams;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UpdateEventRequestWithParticipantParams extends UpdateEventRequestParams {
    private String participantId;
    private ParticipantEventRequest participantEventRequest;
}