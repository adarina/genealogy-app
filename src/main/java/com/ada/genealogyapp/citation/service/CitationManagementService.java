package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CitationManagementService {

    private final TreeService treeService;

    private final CitationService citationService;

    public CitationManagementService(TreeService treeService, CitationService citationService) {
        this.treeService = treeService;
        this.citationService = citationService;
    }

    public Citation validateTreeAndCitation(UUID treeId, UUID citationId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return citationService.findCitationByIdOrThrowNodeNotFoundException(citationId);
    }
}
