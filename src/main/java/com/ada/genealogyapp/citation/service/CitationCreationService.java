package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.relationship.ChildRelationship;
import com.ada.genealogyapp.person.relationship.ChildRelationshipType;
import com.ada.genealogyapp.person.relationship.FamilyRelationship;
import com.ada.genealogyapp.person.relationship.FamilyRelationshipType;
import com.ada.genealogyapp.person.service.PersonManagementService;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import com.ada.genealogyapp.tree.service.TreeSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class CitationCreationService {

    private final TreeRepository treeRepository;

    private final TreeSearchService treeSearchService;

    private final CitationRepository citationRepository;

    public CitationCreationService(TreeRepository treeRepository, TreeSearchService treeSearchService, CitationRepository citationRepository) {
        this.treeRepository = treeRepository;
        this.treeSearchService = treeSearchService;
        this.citationRepository = citationRepository;
    }


    public Citation create(Citation citation) {
        Citation savedCitation = citationRepository.save(citation);
        log.info("Citation created successfully: {}", savedCitation);
        return savedCitation;
    }

    public void createCitation(UUID treeId, CitationRequest citationRequest) {
        Citation citation = CitationRequest.dtoToEntityMapper().apply(citationRequest);

        Tree tree = treeSearchService.findTreeById(treeId);
        citation.setTree(tree);

        create(citation);
        treeRepository.save(tree);

        log.info("Citation created successfully: {}", citation.getPage());
    }
}
