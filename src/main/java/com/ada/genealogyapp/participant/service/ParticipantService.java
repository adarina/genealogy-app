package com.ada.genealogyapp.participant.service;

import com.ada.genealogyapp.participant.model.Participant;

public interface ParticipantService {

    Participant findParticipantById(String participantId);

    void ensureParticipantExists(String participantId);
}
