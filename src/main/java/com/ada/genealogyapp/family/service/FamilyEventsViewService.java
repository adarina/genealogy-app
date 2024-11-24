package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyEventResponse;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyEventsViewService {

    private final TreeService treeService;

    private final FamilyService familyService;

    private final FamilyRepository familyRepository;

    public Page<FamilyEventResponse> getFamilyEvents(UUID treeId, UUID familyId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return familyRepository.findFamilyEvents(familyId, pageable);
    }

    public FamilyEventResponse getFamilyEvent(UUID treeId, UUID familyId, UUID eventId) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return familyRepository.findFamilyEvent(eventId, familyId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and family " + familyId.toString()));

    }
}



