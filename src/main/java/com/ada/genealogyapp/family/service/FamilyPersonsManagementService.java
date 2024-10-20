package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.relationship.ChildRelationship;
import com.ada.genealogyapp.family.type.ChildRelationshipType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class FamilyPersonsManagementService {

    private final TreeTransactionService treeTransactionService;
    private final FamilyManagementService familyManagementService;
    private final PersonSearchService personSearchService;

    public FamilyPersonsManagementService(TreeTransactionService treeTransactionService, FamilyManagementService familyManagementService, PersonSearchService personSearchService) {
        this.treeTransactionService = treeTransactionService;
        this.familyManagementService = familyManagementService;
        this.personSearchService = personSearchService;
    }

    @Transactional
    public void addChildToFamily(UUID treeId, UUID familyId, UUID childId) {

        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person child = personSearchService.findPersonById(childId);

        if (family.getChildren().contains(child)) {
            throw new NodeAlreadyInNodeException("Child " + child.getId() + " is already part of the family " + familyId);
        }
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (c:Person {id: $childId}) " +
                "MERGE (f)-[:HAS_CHILD]->(c)";

        tx.run(cypher, Map.of("familyId", familyId.toString(), "childId", child.getId().toString()));
        log.info("Child {} added successfully to the family {}", child.getFirstname(), family.getId());
    }

    @Transactional
    public void addFatherToFamily(UUID treeId, UUID familyId, UUID fatherId) {
        addExistingParentToFamily(treeId, familyId, fatherId, "HAS_FATHER", "Father");
    }

    @Transactional
    public void addMotherToFamily(UUID treeId, UUID familyId, UUID motherId) {
        addExistingParentToFamily(treeId, familyId, motherId, "HAS_MOTHER", "Mother");
    }

    @Transactional
    public void addExistingParentToFamily(UUID treeId, UUID familyId, UUID personId, String relationshipType, String entityType) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person parent = personSearchService.findPersonById(personId);

        removePersonFromFamily(family, tx, parent);

        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (c:Person {id: $parentId}) " +
                "MERGE (f)-[:" + relationshipType + "]->(c)";

        tx.run(cypher, Map.of("familyId", familyId.toString(), "parentId", parent.getId().toString()));
        log.info("{} {} added successfully to the family {}", entityType, parent.getFirstname(), family.getId());

        addDefaultParentOfChildRelationship(family, tx, parent);
    }

    @Transactional
    public void addDefaultParentOfChildRelationship(Family family, Transaction tx, Person parent) {
        Set<Person> children = family.getChildren();

        for (Person child : children) {
            ChildRelationshipType type = ChildRelationshipType.BIOLOGICAL;

            String cypher = "MATCH (p:Person {id: $parentId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {childRelationshipType: $relationshipType}]->(c)";

            tx.run(cypher, Map.of("parentId", parent.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
        }
        log.info("Default children relationships added for parent {} in family {}", parent.getFirstname(), family.getId());
    }

    @Transactional
    public void removeParentOfChildRelationship(Family family, Transaction tx, Person parent) {
        Set<ChildRelationship> childRelationships = parent.getChildren();

        for (ChildRelationship childRelationship : childRelationships) {
            Person child = childRelationship.getChild();

            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $parentId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (p)-[r:PARENT_OF]->(c) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "parentId", parent.getId().toString(), "childId", child.getId().toString()));
            log.info("Parent-child relationship removed for parent {} and child {}", parent.getFirstname(), child.getFirstname());
        }
    }

    @Transactional
    public void removeChildOfParentRelationship(Family family, Transaction tx, Person child) {
        Person father = family.getFather();
        Person mother = family.getMother();

        if (nonNull(father)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $fatherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (p)-[r:PARENT_OF]->(c) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "fatherId", father.getId().toString(), "childId", child.getId().toString()));
            log.info("Father-child relationship removed for father {} and child {}", father.getFirstname(), child.getFirstname());
        }

        if (nonNull(mother)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $motherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (p)-[r:PARENT_OF]->(c) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "motherId", mother.getId().toString(), "childId", child.getId().toString()));
            log.info("Mother-child relationship removed for mother {} and child {}", mother.getFirstname(), child.getFirstname());
        }
    }

    @Transactional
    public void removePersonFromFamily(UUID treeId, UUID familyId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person person = personSearchService.findPersonById(UUIDRequest.getId());

        removePersonFromFamily(family, tx, person);

    }

    @Transactional
    public void removePersonFromFamily(Family family, Transaction tx, Person person) {
        String relationshipType = determineAndDeleteRelationshipType(family, tx, person);
        if (nonNull(relationshipType)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $personId}) " +
                    "MATCH (f)-[r:" + relationshipType + "]->(p) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "personId", person.getId().toString()));
            log.info("Person {} removed from family {}", person.getFirstname(), family.getId());
        }
    }

    @Transactional
    public String determineAndDeleteRelationshipType(Family family, Transaction tx, Person person) {
        if (person.equals(family.getFather())) {
            removeParentOfChildRelationship(family, tx, person);
            return "HAS_FATHER";
        } else if (person.equals(family.getMother())) {
            removeParentOfChildRelationship(family, tx, person);
            return "HAS_MOTHER";
        } else if (family.getChildren().contains(person)) {
            removeChildOfParentRelationship(family, tx, person);
            return "HAS_CHILD";
        } else {
            return null;
        }
    }
}
