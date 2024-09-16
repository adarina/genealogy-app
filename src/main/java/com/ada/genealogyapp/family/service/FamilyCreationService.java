package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.exceptions.NodeNotFoundException;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.repostitory.FamilyRepository;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class FamilyCreationService {

    private final TreeSearchService treeSearchService;

    private final TreeRepository treeRepository;

    private final FamilyRepository familyRepository;

    public FamilyCreationService(TreeSearchService treeSearchService, TreeRepository treeRepository, FamilyRepository familyRepository) {
        this.treeSearchService = treeSearchService;
        this.treeRepository = treeRepository;
        this.familyRepository = familyRepository;
    }


    public Family create(Family family) {
        Family savedFamily = familyRepository.save(family);
        log.info("Family created successfully: {}", savedFamily);
        return savedFamily;
    }

    public void createFamily(UUID treeId) {
        Family family = new Family();

        Tree tree = treeSearchService.findTreeById(treeId);
        tree.getFamilies().add(family);

        create(family);
        treeRepository.save(tree);
    }
}
