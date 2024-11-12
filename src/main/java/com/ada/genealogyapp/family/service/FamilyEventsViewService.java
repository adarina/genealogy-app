package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyEventResponse;
import com.ada.genealogyapp.family.dto.FamilyEventsResponse;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FamilyEventsViewService {

    private final FamilyManagementService familyManagementService;

    private final FamilyRepository familyRepository;

    public FamilyEventsViewService(FamilyManagementService familyManagementService, FamilyRepository familyRepository) {
        this.familyManagementService = familyManagementService;
        this.familyRepository = familyRepository;
    }

    public Page<FamilyEventsResponse> getFamilyEvents(UUID treeId, UUID familyId, Pageable pageable) {
        familyManagementService.validateTreeAndFamily(treeId, familyId);
        return familyRepository.findFamilyEvents(familyId, pageable);
    }

    public FamilyEventResponse getFamilyEvent(UUID treeId, UUID familyId, UUID eventId) {
        familyManagementService.validateTreeAndFamily(treeId, familyId);
        return familyRepository.findFamilyEvent(eventId, familyId)
                .orElseThrow(() -> new NodeNotFoundException("Event " + eventId.toString() + " not found for tree " + treeId.toString() + " and family " + familyId.toString()));

    }
}



