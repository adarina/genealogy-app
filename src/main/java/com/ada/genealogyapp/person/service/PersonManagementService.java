package com.ada.genealogyapp.person.service;


import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.tree.service.TreeService;
import com.ada.genealogyapp.tree.service.TreeTransactionService;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;

@Service
@Slf4j
public class PersonManagementService {
    private final PersonService personService;

    private final TreeService treeService;

    private final TreeTransactionService treeTransactionService;

    private final EventService eventService;

    public PersonManagementService(PersonService personService, TreeService treeService, TreeTransactionService treeTransactionService, EventService eventService) {
        this.personService = personService;
        this.treeService = treeService;
        this.treeTransactionService = treeTransactionService;
        this.eventService = eventService;
    }

    @Transactional
    public void startTransactionAndSession(UUID treeId, UUID personId) {
        validateTreeAndPerson(treeId, personId);
        treeTransactionService.startTransactionAndSession();
    }

    public Person validateTreeAndPerson(UUID treeId, UUID personId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        return personService.findPersonByIdOrThrowNodeNotFoundException(personId);
    }

    @Transactional
    public void startTransactionAndSession(UUID treeId, UUID personId, UUID eventId) {
        validateTreePersonAndEvent(treeId, personId, eventId);
        treeTransactionService.startTransactionAndSession();
    }

    public Event validateTreePersonAndEvent(UUID treeId, UUID personId, UUID eventId) {
        treeService.findTreeByIdOrThrowNodeNotFoundException(treeId);
        personService.findPersonByIdOrThrowNodeNotFoundException(personId);
        return eventService.findEventByIdOrThrowNodeNotFoundException(eventId);
    }

    @Transactional
    public void updatePersonalData(UUID treeId, UUID personId, PersonRequest personRequest) {
        Transaction tx = treeTransactionService.getCurrentTransaction();
        validateTreeAndPerson(treeId, personId);

        updateFirstname(tx, personId, personRequest.getFirstname());
        updateLastname(tx, personId, personRequest.getLastname());
        updateBirthDate(tx, personId, personRequest.getBirthDate());
        updateGender(tx, personId, personRequest.getGenderType());

        log.info("Person updated successfully: {}", personId);
    }

    private void updateFirstname(Transaction tx, UUID personId, String firstname) {
        if (nonNull(firstname)) {
            String cypher = "MATCH (p:Person {id: $personId}) SET p.firstname = $firstname";
            tx.run(cypher, Map.of("personId", personId.toString(), "firstname", firstname));
        }
    }

    private void updateLastname(Transaction tx, UUID personId, String lastname) {
        if (nonNull(lastname)) {
            String cypher = "MATCH (p:Person {id: $personId}) SET p.lastname = $lastname";
            tx.run(cypher, Map.of("personId", personId.toString(), "lastname", lastname));
        }
    }

    private void updateBirthDate(Transaction tx, UUID personId, LocalDate birthDate) {
        if (nonNull(birthDate)) {
            String cypher = "MATCH (p:Person {id: $personId}) SET p.birthDate = $birthDate";
            tx.run(cypher, Map.of("personId", personId.toString(), "birthDate", birthDate.toString()));
        }
    }

    private void updateGender(Transaction tx, UUID personId, GenderType genderType) {
        if (nonNull(genderType)) {
            String cypher = "MATCH (p:Person {id: $personId}) SET p.genderType = $genderType";
            tx.run(cypher, Map.of("personId", personId.toString(), "genderType", genderType.name()));
        }
    }
}
