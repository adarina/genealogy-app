package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildrenResponse;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class FamilyChildrenViewService {

    private final FamilyRepository familyRepository;

    private final FamilyManagementService familyManagementService;

    public FamilyChildrenViewService(FamilyRepository familyRepository, FamilyManagementService familyManagementService) {
        this.familyRepository = familyRepository;
        this.familyManagementService = familyManagementService;
    }

    public Page<FamilyChildrenResponse> getChildren(UUID treeId, UUID familyId, Pageable pageable) {
        familyManagementService.validateTreeAndFamily(treeId, familyId);
        return familyRepository.findChildren(familyId, pageable);
    }
}
