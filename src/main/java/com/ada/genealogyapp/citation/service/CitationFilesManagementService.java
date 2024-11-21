package com.ada.genealogyapp.citation.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileSearchService;
import com.ada.genealogyapp.tree.service.TreeService;
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

    private final CitationService citationService;

    private final FileSearchService fileSearchService;

    private final TreeService treeService;

    public CitationFilesManagementService(TreeTransactionService treeTransactionService, CitationService citationService, FileSearchService fileSearchService, TreeService treeService) {
        this.treeTransactionService = treeTransactionService;
        this.citationService = citationService;
        this.fileSearchService = fileSearchService;
        this.treeService = treeService;
    }


    @TransactionalInNeo4j
    public void addExistingFileToCitation(UUID treeId, UUID citationId, UUID fileId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        treeService.ensureTreeExists(treeId);
        Citation citation = citationService.findCitationById(citationId);
        File file = fileSearchService.findFileByIdOrThrowNodeNotFoundException(fileId);

        String cypher = "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (f:File {id: $fileId}) " +
                "MERGE (c)-[:HAS_FILE]->(f)";

        tx.run(cypher, Map.of("citationId", citationId.toString(), "fileId", file.getId().toString()));
        log.info("File {} added successfully to the citation {}", file.getName(), citation.getId());
        tx.commit();
    }
}
