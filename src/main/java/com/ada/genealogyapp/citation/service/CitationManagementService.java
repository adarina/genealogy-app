package com.ada.genealogyapp.citation.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

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

    @TransactionalInNeo4j
    public void updateDate(Transaction tx, UUID citationId, LocalDate date) {
        if (nonNull(date)) {
            String cypher = "MATCH (c:Citation {id: $citationId}) SET c.date = $date";
            tx.run(cypher, Map.of("citationId", citationId.toString(), "date", date));
        }
    }

    @TransactionalInNeo4j
    public void updatePage(Transaction tx, UUID citationId, String page) {
        if (nonNull(page)) {
            String cypher = "MATCH (c:Citation {id: $citationId}) SET c.page = $page";
            tx.run(cypher, Map.of("citationId", citationId.toString(), "page", page));
        }
    }
}
