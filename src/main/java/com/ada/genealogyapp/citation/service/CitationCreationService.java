package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CitationCreationService {


    private final TreeService treeService;

    private final CitationService citationService;

    private final CitationValidationService citationValidationService;

    public CitationCreationService(TreeService treeService, CitationService citationService, CitationValidationService citationValidationService) {
        this.treeService = treeService;
        this.citationService = citationService;
        this.citationValidationService = citationValidationService;
    }


    @TransactionalInNeo4j
    public Citation createCitation(String treeId, CitationRequest citationRequest) {
        Tree tree = treeService.findTreeById(treeId);
        Citation citation = Citation.builder()
                .tree(tree)
                .page(citationRequest.getPage())
                .date(citationRequest.getDate())
                .build();

        citationValidationService.validateCitation(citation);

        citationService.saveCitation(citation);
        return citation;
    }
}
