package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildrenResponse;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyChildrenViewService {

    private final FamilyRepository familyRepository;

    private final TreeService treeService;

    private final FamilyService familyService;

    public Page<FamilyChildrenResponse> getChildren(UUID treeId, UUID familyId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return familyRepository.findChildren(familyId, pageable);
    }
}
