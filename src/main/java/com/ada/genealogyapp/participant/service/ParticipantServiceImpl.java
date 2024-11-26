package com.ada.genealogyapp.participant.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.participant.repository.ParticipantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@Slf4j
public class ParticipantServiceImpl implements ParticipantService{

    private final ParticipantRepository participantRepository;

    public ParticipantServiceImpl(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant findParticipantById(UUID participantId) {
        return participantRepository.findById(participantId)
                .orElseThrow(() -> new NodeNotFoundException("Participant not found with ID: " + participantId));
    }
}
