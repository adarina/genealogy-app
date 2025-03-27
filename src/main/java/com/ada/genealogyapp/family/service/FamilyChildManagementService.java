package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class FamilyChildManagementService {

    private final FamilyService familyService;

    @TransactionalInNeo4j
    public void addChildToFamily(AddChildToFamilyRequestParams params) {
        familyService.addChildToFamily(params);
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(RemovePersonFromFamilyParams params) {
        familyService.removeChildFromFamily(params);
    }
}