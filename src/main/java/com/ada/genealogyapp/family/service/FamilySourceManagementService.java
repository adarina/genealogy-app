package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FamilySourceManagementService {

    private final TreeTransactionService treeTransactionService;

    private final FamilyManagementService familyManagementService;

    private final CitationService citationService;

    public FamilySourceManagementService(TreeTransactionService treeTransactionService, FamilyManagementService familyManagementService, CitationService citationService) {
        this.treeTransactionService = treeTransactionService;
        this.familyManagementService = familyManagementService;
        this.citationService = citationService;
    }

    @Transactional
    public void addSourceToFamily(UUID treeId, UUID familyId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Citation citation = citationService.findCitationById(UUIDRequest.getId());

        if (family.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the family " + familyId);
        }
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (f)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("familyId", familyId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the family {}", citation.getPage(), family.getId());

    }

    @Transactional
    public void removeSourceFromFamily(UUID treeId, UUID familyId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Citation citation = citationService.findCitationById(UUIDRequest.getId());

        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (f)-[r:HAS_CITATION]->(c) " +
                "DELETE r";

        tx.run(cypher, Map.of("familyId", family.getId().toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} removed from family {}", citation.getId(), family.getId());

    }
}
