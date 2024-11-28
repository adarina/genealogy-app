package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.participant.dto.ParticipantEventResponse;
import com.ada.genealogyapp.participant.repository.ParticipantRepository;
import com.ada.genealogyapp.person.repostitory.PersonRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FamilyEventsViewService {

    private final TreeService treeService;

    private final FamilyService familyService;

    private final FamilyRepository familyRepository;

    private final PersonRepository personRepository;

    private final ParticipantRepository participantRepository;

    public FamilyEventsViewService(TreeService treeService, FamilyService familyService, FamilyRepository familyRepository, PersonRepository personRepository, ParticipantRepository participantRepository) {
        this.treeService = treeService;
        this.familyService = familyService;
        this.familyRepository = familyRepository;
        this.personRepository = personRepository;
        this.participantRepository = participantRepository;
    }

    public Page<ParticipantEventResponse> getFamilyEvents(String treeId, String familyId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return participantRepository.findParticipantEvents(familyId, pageable);
    }

    public ParticipantEventResponse getFamilyEvent(String treeId, String familyId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return participantRepository.findParticipantEvent(eventId, familyId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and family " + familyId.toString()));

    }
}



