package com.ada.genealogyapp.participant.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseParticipantParams {
    private String userId;
    private String treeId;
    private String participantId;
}