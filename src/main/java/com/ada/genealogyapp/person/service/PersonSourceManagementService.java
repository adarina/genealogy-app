package com.ada.genealogyapp.person.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PersonSourceManagementService {

    private final TreeTransactionService treeTransactionService;

    private final PersonManagementService personManagementService;

    private final CitationService citationService;

    public PersonSourceManagementService(TreeTransactionService treeTransactionService, PersonManagementService personManagementService, CitationService citationService) {
        this.treeTransactionService = treeTransactionService;
        this.personManagementService = personManagementService;
        this.citationService = citationService;
    }


    @Transactional
    public void addSourceToPerson(UUID treeId, UUID personId, UUID citationId) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Citation citation = citationService.findCitationById(citationId);

        if (person.getCitations().contains(citation)) {
            throw new NodeAlreadyInNodeException("Citation " + citation.getId() + " is already part of the person " + personId);
        }
        String cypher = "MATCH (p:Person {id: $personId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MERGE (p)-[:HAS_CITATION]->(c)";

        tx.run(cypher, Map.of("personId", personId.toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} added successfully to the person {}", citation.getPage(), person.getId());
    }

    @Transactional
    public void removeSourceFromPerson(UUID treeId, UUID personId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Person person = personManagementService.validateTreeAndPerson(treeId, personId);
        Citation citation = citationService.findCitationById(UUIDRequest.getId());

        String cypher = "MATCH (p:Person {id: $personId}) " +
                "MATCH (c:Citation {id: $citationId}) " +
                "MATCH (p)-[r:HAS_CITATION]->(c) " +
                "DELETE r";

        tx.run(cypher, Map.of("personId", person.getId().toString(), "citationId", citation.getId().toString()));
        log.info("Citation {} removed from person {}", citation.getId(), person.getId());
    }
}
