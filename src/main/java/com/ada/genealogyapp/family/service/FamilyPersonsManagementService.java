package com.ada.genealogyapp.family.service;

import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.exceptions.NodeAlreadyInNodeException;
import com.ada.genealogyapp.family.dto.UUIDRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonSearchService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

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


    @TransactionalInNeo4j
    public void addChildToFamily(UUID treeId, UUID familyId, UUID childId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person child = personSearchService.findPersonByIdOrThrowNodeNotFoundException(childId);

        if (family.getChildren().contains(child)) {
            throw new NodeAlreadyInNodeException("Child " + child.getId() + " is already part of the family " + familyId);
        }
        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (p:Person {id: $childId}) " +
                "MERGE (p)-[:HAS_FAMILY]->(f)" +
                "MERGE (f)-[:HAS_CHILD]->(p)";

        tx.run(cypher, Map.of("familyId", familyId.toString(), "childId", child.getId().toString()));
        log.info("Child {} added successfully to the family {}", child.getFirstname(), family.getId());

        addDefaultChildOfParentRelationshipWhenAddingChild(family, tx, child);
        addDefaultParentOfParentRelationshipWhenAddingChild(family, tx, child);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void addFatherToFamily(UUID treeId, UUID familyId, UUID fatherId) {
        addExistingParentToFamily(treeId, familyId, fatherId, "HAS_FATHER", "Father");
    }

    @TransactionalInNeo4j
    public void addMotherToFamily(UUID treeId, UUID familyId, UUID motherId) {
        addExistingParentToFamily(treeId, familyId, motherId, "HAS_MOTHER", "Mother");
    }

    @TransactionalInNeo4j
    public void addExistingParentToFamily(UUID treeId, UUID familyId, UUID personId, String relationshipType, String entityType) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person parent = personSearchService.findPersonByIdOrThrowNodeNotFoundException(personId);

        if (parent.equals(family.getMother()) || parent.equals(family.getFather())) {
            throw new NodeAlreadyInNodeException("Parent is already in family: " + parent.getId().toString());
        }
        removePersonFromFamily(family, tx, parent);

        String cypher = "MATCH (f:Family {id: $familyId}) " +
                "MATCH (p:Person {id: $parentId}) " +
                "MERGE (p)-[:HAS_FAMILY]->(f) " +
                "MERGE (f)-[:" + relationshipType + "]->(p) " +
                "WITH f " +
                "OPTIONAL MATCH (f)-[:HAS_FATHER]->(father:Person) " +
                "OPTIONAL MATCH (f)-[:HAS_MOTHER]->(mother:Person) " +
                "SET f.name = COALESCE(father.name, 'null') + ' & ' + COALESCE(mother.name, 'null')";


        tx.run(cypher, Map.of("familyId", familyId.toString(), "parentId", parent.getId().toString()));
        log.info("{} {} added successfully to the family {}", entityType, parent.getFirstname(), family.getId());

        addDefaultParentOfChildRelationshipWhenAddingParent(family, tx, parent);
        addDefaultChildOfParentRelationshipWhenAddingParent(family, tx, parent);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void addDefaultParentOfChildRelationshipWhenAddingParent(Family family, Transaction tx, Person parent) {
        Set<Person> children = family.getChildren();

        for (Person child : children) {
            PersonRelationshipType type = PersonRelationshipType.BIOLOGICAL;

            String cypher = "MATCH (p:Person {id: $parentId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {personRelationshipType: $relationshipType}]->(c)";

            tx.run(cypher, Map.of("parentId", parent.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
        }
        log.info("Default person relationships added for parent {} in family {}", parent.getFirstname(), family.getId());
    }

    @TransactionalInNeo4j
    public void addDefaultChildOfParentRelationshipWhenAddingParent(Family family, Transaction tx, Person parent) {
        Set<Person> children = family.getChildren();

        for (Person child : children) {
            PersonRelationshipType type = PersonRelationshipType.BIOLOGICAL;

            String cypher = "MATCH (p:Person {id: $parentId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (c)-[r:CHILD_OF {personRelationshipType: $relationshipType}]->(p)";

            tx.run(cypher, Map.of("parentId", parent.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
        }
        log.info("Default person relationships added for child {} in family {}", parent.getFirstname(), family.getId());

    }

    @TransactionalInNeo4j
    public void addDefaultChildOfParentRelationshipWhenAddingChild(Family family, Transaction tx, Person child) {

        Person father = family.getFather();
        Person mother = family.getMother();
        PersonRelationshipType type = PersonRelationshipType.BIOLOGICAL;

        if (nonNull(father)) {
            String cypher = "MATCH (p:Person {id: $fatherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (c)-[r:CHILD_OF {personRelationshipType: $relationshipType}]->(p)";

            tx.run(cypher, Map.of("fatherId", father.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }

        if (nonNull(mother)) {
            String cypher = "MATCH (p:Person {id: $motherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (c)-[r:CHILD_OF {personRelationshipType: $relationshipType}]->(p)";

            tx.run(cypher, Map.of("motherId", mother.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }
    }

    @TransactionalInNeo4j
    public void addDefaultParentOfParentRelationshipWhenAddingChild(Family family, Transaction tx, Person child) {

        Person father = family.getFather();
        Person mother = family.getMother();
        PersonRelationshipType type = PersonRelationshipType.BIOLOGICAL;

        if (nonNull(father)) {
            String cypher = "MATCH (p:Person {id: $fatherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {personRelationshipType: $relationshipType}]->(c)";

            tx.run(cypher, Map.of("fatherId", father.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }

        if (nonNull(mother)) {
            String cypher = "MATCH (p:Person {id: $motherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MERGE (p)-[r:PARENT_OF {personRelationshipType: $relationshipType}]->(c)";

            tx.run(cypher, Map.of("motherId", mother.getId().toString(), "childId", child.getId().toString(), "relationshipType", type.name()));
            log.info("Default person relationships added for child {} in family {}", child.getFirstname(), family.getId());
        }

    }

    @TransactionalInNeo4j
    public void removeParentOfChildRelationship(Family family, Transaction tx, Person parent) {
        Set<PersonRelationship> personRelationships = parent.getChildren();

        for (PersonRelationship personRelationship : personRelationships) {
            Person child = personRelationship.getChild();

            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $parentId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (p)-[r1:PARENT_OF]->(c) " +
                    "MATCH (c)-[r2:CHILD_OF]->(p) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r1, r2";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "parentId", parent.getId().toString(), "childId", child.getId().toString()));
            log.info("Parent-child relationship removed for parent {} and child {}", parent.getFirstname(), child.getFirstname());
        }
    }

    @TransactionalInNeo4j
    public void removeChildOfParentRelationship(Family family, Transaction tx, Person child) {
        Person father = family.getFather();
        Person mother = family.getMother();

        if (nonNull(father)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $fatherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (c)-[r:CHILD_OF]->(p) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "fatherId", father.getId().toString(), "childId", child.getId().toString()));
            log.info("Father-child relationship removed for father {} and child {}", father.getFirstname(), child.getFirstname());
        }

        if (nonNull(mother)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $motherId}) " +
                    "MATCH (c:Person {id: $childId}) " +
                    "MATCH (c)-[r:CHILD_OF]->(p) " +
                    "WHERE (f)-[:HAS_CHILD]->(c) " +
                    "DELETE r";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "motherId", mother.getId().toString(), "childId", child.getId().toString()));
            log.info("Mother-child relationship removed for mother {} and child {}", mother.getFirstname(), child.getFirstname());
        }
    }

    @TransactionalInNeo4j
    public void removePersonFromFamily(UUID treeId, UUID familyId, UUIDRequest UUIDRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person person = personSearchService.findPersonByIdOrThrowNodeNotFoundException(UUIDRequest.getId());

        removePersonFromFamily(family, tx, person);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removePersonFromFamily(Family family, Transaction tx, Person person) {
        String relationshipType = determineAndDeleteRelationshipType(family, tx, person);
        if (nonNull(relationshipType)) {
            String cypher = "MATCH (f:Family {id: $familyId}) " +
                    "MATCH (p:Person {id: $personId}) " +
                    "MATCH (p)-[r1:HAS_FAMILY]->(f)" +
                    "MATCH (f)-[r2:" + relationshipType + "]->(p) " +
                    "DELETE r1, r2";

            tx.run(cypher, Map.of("familyId", family.getId().toString(), "personId", person.getId().toString()));
            log.info("Person {} removed from family {}", person.getFirstname(), family.getId());

            String updateFamilyNameCypher = "MATCH (f:Family {id: $familyId}) " +
                    "OPTIONAL MATCH (f)-[:HAS_FATHER]->(father:Person) " +
                    "OPTIONAL MATCH (f)-[:HAS_MOTHER]->(mother:Person) " +
                    "SET f.name = COALESCE(father.firstname, 'null') + ' & ' + COALESCE(mother.firstname, 'null')";

            tx.run(updateFamilyNameCypher, Map.of("familyId", family.getId().toString()));
            log.info("Updated family name: {}", family.getName());
        }
    }

    @TransactionalInNeo4j
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
