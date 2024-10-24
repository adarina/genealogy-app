package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileSearchService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class CitationFilesManagementService {

    private final TreeTransactionService treeTransactionService;

    private final CitationManagementService citationManagementService;

    private final FileSearchService fileSearchService;

    public CitationFilesManagementService(TreeTransactionService treeTransactionService, CitationManagementService citationManagementService, FileSearchService fileSearchService) {
        this.treeTransactionService = treeTransactionService;
        this.citationManagementService = citationManagementService;
        this.fileSearchService = fileSearchService;
    }


    @TransactionalInNeo4j
    public void addExistingFileToCitation(UUID treeId, UUID citationId, UUID fileId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Citation citation = citationManagementService.validateTreeAndCitation(treeId, citationId);
        File file = fileSearchService.findFileById(fileId);

        String cypher = "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (f:File {id: $fileId}) " +
                "MERGE (c)-[:HAS_FILE]->(f)";

        tx.run(cypher, Map.of("citationId", citationId.toString(), "fileId", file.getId().toString()));
        log.info("File {} added successfully to the citation {}", file.getName(), citation.getId());
        tx.commit();
    }
}
