package com.ada.genealogyapp.event.dto.params;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AddParticipantToEventParams extends BaseEventParams {
    private String participantId;
    private String relationshipType;
}
