package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.dto.FamilyFilterRequest;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
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
    private final TreeRepository treeRepository;


    public FamilyViewService(FamilyRepository familyRepository, ObjectMapper objectMapper, TreeRepository treeRepository) {
        this.familyRepository = familyRepository;
        this.objectMapper = objectMapper;
        this.treeRepository = treeRepository;
    }

    public Page<FamilyResponse> getFamilies(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        FamilyFilterRequest filterRequest = objectMapper.readValue(filter, FamilyFilterRequest.class);
        treeRepository.findById(treeId).orElseThrow(() ->
                new EntityNotFoundException("Tree with ID " + treeId + " not found"));
        return familyRepository.findByTreeIdAndFilteredParentNamesAndStatus(
                treeId,
                Optional.ofNullable(filterRequest.getMotherName()).orElse(""),
                Optional.ofNullable(filterRequest.getFatherName()).orElse(""),
                Optional.ofNullable(filterRequest.getStatus()).orElse(""),
                pageable
        );
    }

    public FamilyResponse getFamily(UUID treeId, UUID familyId) {
        return familyRepository.findByTreeIdAndFamilyId(treeId, familyId)
                .orElseThrow(() -> new NodeNotFoundException("Family " + familyId.toString() + " not found for tree " + treeId.toString()));
    }
}
