package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.repository.CitationRepository;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CitationCreationService {


    private final TreeService treeService;

    private final CitationRepository citationRepository;


    public CitationCreationService(TreeService treeService, CitationRepository citationRepository) {
        this.treeService = treeService;
        this.citationRepository = citationRepository;
    }

    @TransactionalInNeo4j
    public Citation createCitation(UUID treeId, CitationRequest citationRequest) {
        Citation citation = CitationRequest.dtoToEntityMapper().apply(citationRequest);

        Tree tree = treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        citation.setTree(tree);

        citationRepository.save(citation);
        treeService.saveTree(tree);

        log.info("Citation created successfully: {}", citation.getPage());

        return citation;
    }
}
