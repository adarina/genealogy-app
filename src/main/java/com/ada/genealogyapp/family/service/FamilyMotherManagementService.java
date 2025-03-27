package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.RemovePersonFromFamilyParams;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyMotherManagementService {

    private final FamilyService familyService;

    @TransactionalInNeo4j
    public void addMotherToFamily(AddPersonToFamilyParams params) {
        familyService.addMotherToFamily(params);
    }

    @TransactionalInNeo4j
    public void removeMotherFromFamily(RemovePersonFromFamilyParams params) {
        familyService.removeMotherFromFamily(params);
    }
}
