package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class FamilyMemberCreationService {

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final FamilySearchService familySearchService;

    private final PersonCreationService personCreationService;

    public FamilyMemberCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, FamilySearchService familySearchService, PersonCreationService personCreationService) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.familySearchService = familySearchService;
        this.personCreationService = personCreationService;
    }


    public void createFamilyMember(UUID treeId, UUID familyId, PersonRequest personRequest) {
        Person person = PersonRequest.dtoToEntityMapper().apply(personRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        familySearchService.findFamilyById(familyId);

        personCreationService.create(person);
        treeRepository.save(tree);

        log.info("Member created successfully: {}", person.getId());
    }
}
