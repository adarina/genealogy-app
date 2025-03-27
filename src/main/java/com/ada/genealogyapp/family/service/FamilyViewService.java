package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamiliesResponse;
import com.ada.genealogyapp.family.dto.FamilyFilterRequest;
import com.ada.genealogyapp.family.dto.FamilyResponse;
import com.ada.genealogyapp.family.dto.params.GetFamiliesParams;
import com.ada.genealogyapp.family.dto.params.GetFamilyParams;
import com.ada.genealogyapp.family.repository.FamilyRepository;
import com.ada.genealogyapp.tree.service.TreeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyViewService {

    private final ObjectMapper objectMapper;

    private final TreeService treeService;

    private final FamilyRepository familyRepository;


    public Page<FamiliesResponse> getFamilies(GetFamiliesParams params) throws JsonProcessingException {
        FamilyFilterRequest filterRequest = objectMapper.readValue(params.getFilter(), FamilyFilterRequest.class);
        Page<FamiliesResponse> page = familyRepository.find(params.getUserId(), params.getTreeId(), filterRequest.getFatherName(), filterRequest.getMotherName(), filterRequest.getStatus(), params.getPageable());
        treeService.ensureUserAndTreeExist(params, page);
        return page;
    }

    public FamilyResponse getFamily(GetFamilyParams params) {
        FamilyResponse familyResponse = familyRepository.find(params.getUserId(), params.getTreeId(), params.getFamilyId());
        treeService.ensureUserAndTreeExist(params, familyResponse);
        return familyResponse;
    }
}