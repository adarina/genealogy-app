package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class FamilyFatherManagementService {

    private final FamilyService familyService;

    @TransactionalInNeo4j
    public void addFatherToFamily(String treeId, String familyId, String fatherId) {
        familyService.addFatherToFamily(treeId, familyId, fatherId);
    }

    @TransactionalInNeo4j
    public void removeFatherFromFamily(String treeId, String familyId, String fatherId) {
        familyService.removeFatherFromFamily(treeId, familyId, fatherId);
    }
}
