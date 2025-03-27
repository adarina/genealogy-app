package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyChildResponse;
import com.ada.genealogyapp.family.dto.params.GetChildrenParams;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyChildrenViewService {

    private final FamilyRepository familyRepository;

    private final TreeService treeService;

    public Page<FamilyChildResponse> getChildren(GetChildrenParams params) {
        Page<FamilyChildResponse> page = familyRepository.findChildren(params.getUserId(), params.getTreeId(), params.getFamilyId(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }
}