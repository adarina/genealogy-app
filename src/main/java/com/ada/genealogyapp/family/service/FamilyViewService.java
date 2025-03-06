package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.dto.FamilyFilterRequest;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class FamilyViewService {


    private final FamilyRepository familyRepository;
    private final ObjectMapper objectMapper;
    private final TreeService treeService;
    private final FamilyService familyService;

    public FamilyViewService(FamilyRepository familyRepository, ObjectMapper objectMapper, TreeService treeService, FamilyService familyService) {
        this.familyRepository = familyRepository;
        this.objectMapper = objectMapper;
        this.treeService = treeService;
        this.familyService = familyService;
    }

    public Page<FamilyResponse> getFamilies(String treeId, String filter, Pageable pageable) throws JsonProcessingException {
        treeService.ensureTreeExists(treeId);
        FamilyFilterRequest filterRequest = objectMapper.readValue(filter, FamilyFilterRequest.class);

        return familyRepository.findByTreeIdAndFilteredParentNamesAndStatus(
                treeId,
                Optional.ofNullable(filterRequest.getMotherName()).orElse(""),
                Optional.ofNullable(filterRequest.getFatherName()).orElse(""),
                Optional.ofNullable(filterRequest.getStatus()).orElse(""),
                pageable
        );
    }

    public FamilyResponse getFamily(String treeId, String familyId) {
        treeService.ensureTreeExists(treeId);
        familyService.ensureFamilyExists(familyId);

        return familyRepository.findByTreeIdAndFamilyId(treeId, familyId)
                .orElseThrow(() -> new NodeNotFoundException("Family " + familyId.toString() + " not found for tree " + treeId.toString()));
    }
}
