package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CitationSourceManagementService {

    private final CitationService citationService;

    @TransactionalInNeo4j
    public void addSourceToCitation(String userId, String treeId, String citationId, String sourceId) {
        citationService.addSourceToCitation(userId, treeId, citationId, sourceId);
    }

    @TransactionalInNeo4j
    public void removeSourceFromCitation(String userId, String treeId, String citationId, String sourceId) {
        citationService.removeSourceFromCitation(userId, treeId, citationId, sourceId);
    }
}
