package com.ada.genealogyapp.family.service;


import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonManagementService;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.tree.service.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class FamilyChildManagementService {


    private final TreeTransactionService treeTransactionService;

    private final PersonManagementService personManagementService;

    private final FamilyManagementService familyManagementService;

    private final PersonService personService;

    public FamilyChildManagementService(TreeTransactionService treeTransactionService, PersonManagementService personManagementService, FamilyManagementService familyManagementService, PersonService personService) {
        this.treeTransactionService = treeTransactionService;
        this.personManagementService = personManagementService;
        this.familyManagementService = familyManagementService;
        this.personService = personService;
    }

    @TransactionalInNeo4j
    public void updateFamilyChild(UUID treeId, UUID familyId, UUID childId, FamilyChildRequest familyChildRequest) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        familyManagementService.validateTreeAndFamily(treeId, familyId);

        personManagementService.updateFirstname(tx, childId, familyChildRequest.getFirstname());
        personManagementService.updateLastname(tx, childId, familyChildRequest.getLastname());
        personManagementService.updateName(tx, childId, familyChildRequest.getFirstname() + " " + familyChildRequest.getLastname());
        personManagementService.updateBirthDate(tx, childId, familyChildRequest.getBirthdate());
        personManagementService.updateGender(tx, childId, familyChildRequest.getGender());

        String cypher = "MATCH (family:Family)-[r:HAS_CHILD]->(child:Person) " +
                "WHERE child.id = $childId AND family.id = $familyId " +
                "OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person) " +
                "OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person) " +
                "OPTIONAL MATCH (father)-[r1:PARENT_OF]->(child) " +
                "OPTIONAL MATCH (mother)-[r2:PARENT_OF]->(child) " +
                "WITH r1, r2 " +
                "SET r1.relationship = CASE WHEN r1 IS NOT NULL THEN $fatherRelationship ELSE r1.relationship END, " +
                "    r2.relationship = CASE WHEN r2 IS NOT NULL THEN $motherRelationship ELSE r2.relationship END ";


        tx.run(cypher, Map.of(
                "familyId", familyId.toString(),
                "childId", childId.toString(),
                "fatherRelationship", familyChildRequest.getFatherRelationship().name(),
                "motherRelationship", familyChildRequest.getMotherRelationship().name()));

        log.info("Child updated successfully: {}", childId);
        tx.commit();
    }

    @TransactionalInNeo4j
    public void removeChildFromFamily(UUID treeId, UUID familyId, UUID childId) {
        Transaction tx = treeTransactionService.startTransactionAndSession();
        Family family = familyManagementService.validateTreeAndFamily(treeId, familyId);
        Person child = personService.findPersonByIdOrThrowNodeNotFoundException(childId);

        String cypher = "MATCH (family:Family)-[r:HAS_CHILD]->(child:Person) " +
                "WHERE child.id = $childId AND family.id = $familyId " +
                "OPTIONAL MATCH (family)-[:HAS_FATHER]->(father:Person) " +
                "OPTIONAL MATCH (family)-[:HAS_MOTHER]->(mother:Person) " +
                "OPTIONAL MATCH (father)-[r1:PARENT_OF]->(child) " +
                "OPTIONAL MATCH (mother)-[r2:PARENT_OF]->(child) " +
                "DELETE r, r1, r2";

        tx.run(cypher, Map.of(
                "familyId", family.getId().toString(),
                "childId", child.getId().toString()));

        log.info("Child {} removed from family {}", child.getId(), family.getId());
        tx.commit();
    }
}