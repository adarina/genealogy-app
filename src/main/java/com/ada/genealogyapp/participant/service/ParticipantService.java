package com.ada.genealogyapp.participant.service;

import com.ada.genealogyapp.participant.model.Participant;

import java.util.UUID;

public interface ParticipantService {

    Participant findParticipantById(UUID participantId);
}
