package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyChildrenViewService {

    private final FamilyRepository familyRepository;

    private final TreeService treeService;

    private final FamilyService familyService;

    public Page<FamilyChildResponse> getChildren(String treeId, String familyId, Pageable pageable) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);
        return familyRepository.findChildren(familyId, pageable);
    }
}