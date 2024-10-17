package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FamilyMemberCreationService {


    private final FamilyService familyService;

    private final PersonCreationService personCreationService;

    private final TreeService treeService;

    public FamilyMemberCreationService(FamilyService familyService, PersonCreationService personCreationService, TreeService treeService) {
        this.familyService = familyService;
        this.personCreationService = personCreationService;
        this.treeService = treeService;
    }

    public void createFamilyMember(UUID treeId, UUID familyId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        familyService.findFamilyByIdOrThrowNodeNotFoundException(familyId);

        personCreationService.savePerson(person);
        treeService.saveTree(tree);

        log.info("Member created successfully: {}", person.getId());
    }
}
