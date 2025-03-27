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
public class FamilyFatherManagementService {

    private final FamilyService familyService;

    @TransactionalInNeo4j
    public void addFatherToFamily(AddPersonToFamilyParams params) {
        familyService.addFatherToFamily(params);
    }

    @TransactionalInNeo4j
    public void removeFatherFromFamily(RemovePersonFromFamilyParams params) {
        familyService.removeFatherFromFamily(params);
    }
}
