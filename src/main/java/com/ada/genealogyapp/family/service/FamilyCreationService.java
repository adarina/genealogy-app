package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.FamilyJsonRequest;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.SaveFamilyParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.family.model.Family;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyCreationService {

    private final FamilyService familyService;

    private final FamilyValidationService familyValidationService;

    @TransactionalInNeo4j
    public Family createFamily(CreateFamilyRequestParams params) {
        Family family = Family.builder()
                .status(params.getFamilyRequest().getStatus())
                .name("null & null")
                .build();
        familyValidationService.validateFamily(family);
        familyService.saveFamily(SaveFamilyParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .familyId(family.getId())
                .family(family)
                .build());
        return family;
    }

    @TransactionalInNeo4j
    public Map<String, Family> createFamilies(String userId, String treeId, List<FamilyJsonRequest> familyRequests) {
        Map<String, Family> createdFamiliesMap = new HashMap<>();
        List<Map<String, Object>> families = new ArrayList<>();

        for (FamilyJsonRequest request : familyRequests) {
            Family family = Family.builder()
                    .name(request.getName())
                    .status(request.getStatus())
                    .build();
            familyValidationService.validateFamily(family);

            Map<String, Object> familyData = new HashMap<>();
            familyData.put("id", family.getId());
            familyData.put("name", family.getName());
            familyData.put("status", family.getStatus().name());

            families.add(familyData);
            createdFamiliesMap.put(request.getId(), family);
        }
        familyService.saveFamilies(userId, treeId, families);
        return createdFamiliesMap;
    }
}
