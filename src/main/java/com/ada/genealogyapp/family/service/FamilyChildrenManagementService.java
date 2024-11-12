package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FamilyChildrenManagementService {

    private final TreeTransactionService treeTransactionService;
    private final FamilyManagementService familyManagementService;
    private final PersonSearchService personSearchService;

    public FamilyChildrenManagementService(TreeTransactionService treeTransactionService, FamilyManagementService familyManagementService, PersonSearchService personSearchService) {
        this.treeTransactionService = treeTransactionService;
        this.familyManagementService = familyManagementService;
        this.personSearchService = personSearchService;
    }

    @TransactionalInNeo4j
    public void addExistingChildToFamily(UUID treeId, UUID familyId, UUID childId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person child = personSearchService.findPersonByIdOrThrowNodeNotFoundException(childId);

        if (family.getChildren().contains(child)) {
            throw new NodeAlreadyInNodeException("Child " + child.getId() + " is already part of the family " + familyId);
        }
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (p:Person {id: $childId}) " +
                "MERGE (f)-[:HAS_CHILD]->(p)";

        tx.run(cypher, Map.of(
                "familyId", familyId.toString(),
                "childId", child.getId().toString()));
        log.info("Child {} added successfully to the family {}", child.getFirstname(), family.getId());

        addDefaultParentOfParentRelationshipWhenAddingChild(family, tx, child);
        tx.commit();
    }

    //TODO możliwość wyboru, jeszcze request tam jest mother i father rel
    @TransactionalInNeo4j
    public void addDefaultParentOfParentRelationshipWhenAddingChild(Family family, Transaction tx, Person child) {

        Person father = family.getFather();
        Person mother = family.getMother();
        PersonRelationshipType relationship = PersonRelationshipType.BIOLOGICAL;

        if (nonNull(father)) {
            String cypher = "MATCH (p:Person {id: $fatherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {relationship: $relationship}]->(c)";

            tx.run(cypher, Map.of(
                    "fatherId", father.getId().toString(),
                    "childId", child.getId().toString(),
                    "relationship", relationship.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }

        if (nonNull(mother)) {
            String cypher = "MATCH (p:Person {id: $motherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {relationship: $relationship}]->(c)";

            tx.run(cypher, Map.of(
                    "motherId", mother.getId().toString(),
                    "childId", child.getId().toString(),
                    "relationship", relationship.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }
    }
}
