package com.ada.genealogyapp.person.dto.params;

import com.ada.genealogyapp.participant.dto.GetParticipantParams;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class GetParticipantEventParams extends GetParticipantParams {
    private String eventId;
}
