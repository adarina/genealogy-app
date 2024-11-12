package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamiliesResponse;
import com.ada.genealogyapp.family.dto.FamilyFilterRequest;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.family.type.StatusType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class FamilyViewService {


    private final FamilyRepository familyRepository;

    public FamilyViewService(FamilyRepository familyRepository) {
        this.familyRepository = familyRepository;
    }


    public Page<FamiliesResponse> getFamilies(UUID treeId, String filter, Pageable pageable) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        FamilyFilterRequest filterRequest = objectMapper.readValue(filter, FamilyFilterRequest.class);

        String motherName = filterRequest.getMotherName();
        String fatherName = filterRequest.getFatherName();
        StatusType status = null;

        if (nonNull(filterRequest.getStatus())) {
            try {
                status = StatusType.valueOf(filterRequest.getStatus().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid status value: {}", filterRequest.getStatus());
            }
        }

        Page<FamiliesResponse> familyPage;
        if (nonNull(status)) {
            familyPage = familyRepository.findByTreeIdWithParentsAndMotherNameContainingIgnoreCaseAndFatherNameContainingIgnoreCaseAndStatusContaining(treeId, motherName != null ? motherName : "", fatherName != null ? fatherName : "", status, pageable);
        } else {
            familyPage = familyRepository.findByTreeIdWithParentsAndMotherNameContainingIgnoreCaseAndFatherNameContainingIgnoreCase(treeId, motherName != null ? motherName : "", fatherName != null ? fatherName : "", pageable);
        }
        return familyPage;
    }
}
