package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;


@Slf4j
@Service
public class CitationSourcesManagementService {

    private final TreeTransactionService treeTransactionService;
    private final SourceService sourceService;

    private final CitationManagementService citationManagementService;

    public CitationSourcesManagementService(TreeTransactionService treeTransactionService, SourceService sourceService, CitationManagementService citationManagementService) {
        this.treeTransactionService = treeTransactionService;
        this.sourceService = sourceService;
        this.citationManagementService = citationManagementService;
    }

    @TransactionalInNeo4j
    public void addExistingSourceToCitation(UUID treeId, UUID citationId, UUID sourceId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Citation citation = citationManagementService.validateTreeAndCitation(treeId, citationId);
        Source source = sourceService.findSourceByIdOrThrowNodeNotFoundException(sourceId);

        String cypher = "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (s:Source {id: $sourceId}) " +
                "MERGE (c)-[:HAS_SOURCE]->(s)";

        tx.run(cypher, Map.of("citationId", citationId.toString(), "sourceId", source.getId().toString()));
        log.info("Source {} added successfully to the citation {}", source.getName(), citation.getId());
        tx.commit();
    }
}
