package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.dto.EventParticipantRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.dto.FamilyJsonRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.file.dto.FileJsonRequest;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileCreationService;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonRelationshipRequest;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.dto.SourceJsonRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.tree.dto.TreeImportJsonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.ada.genealogyapp.tree.repository.TreeRepository;

import java.util.*;

import static java.util.Objects.nonNull;


@Service
@RequiredArgsConstructor
public class TreeImportJsonService {

    private final TreeRepository treeRepository;

    private final PersonService personService;

    private final FamilyService familyService;

    private final PersonCreationService personCreationService;

    private final FamilyCreationService familyCreationService;

    private final EventCreationService eventCreationService;

    private final CitationCreationService citationCreationService;

    private final SourceCreationService sourceCreationService;

    private final EventService eventService;

    private final FileCreationService fileCreationService;

    private final GraphUserRepository graphUserRepository;

    @TransactionalInNeo4j
    public Tree importTree(TreeImportJsonRequest importRequest) {
        GraphUser graphUser = graphUserRepository.find("1");
        Tree tree = initializeTree(importRequest, graphUser);
        Map<String, Person> personMap = processPersons(importRequest, tree, graphUser.getId());
        Map<String, Participant> participantMap = new HashMap<>(personMap);
        processPersonRelationships(tree.getId(), importRequest, personMap);

        Map<String, Family> familyMap = processFamilies(importRequest, tree, personMap, participantMap, graphUser.getId());

        Map<String, Source> sourceMap = processSources(importRequest, tree, graphUser.getId());

        Map<String, File> fileMap = processFiles(importRequest, tree, graphUser.getId());

        Map<String, Citation> citationMap = processCitations(importRequest, tree, sourceMap, fileMap, graphUser.getId());

        Map<String, Event> eventMap = processEvents(importRequest, tree, graphUser.getId());
        processEventRelationships(tree, importRequest, eventMap, participantMap, citationMap, graphUser.getId());

        return tree;
    }

    private Tree initializeTree(TreeImportJsonRequest importRequest, GraphUser graphUser) {

        Tree tree = Tree.builder()
                .name(importRequest.getTree().getName())
                .graphUser(graphUser)
                .build();
        treeRepository.save(tree);
        return tree;
    }

    private Map<String, Person> processPersons(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Person> personMap = new HashMap<>();
        for (PersonJsonRequest personJsonRequest : importRequest.getPersons()) {
            Person person = personCreationService.createPerson(userId, tree, personJsonRequest.getFirstname(), personJsonRequest.getLastname(), personJsonRequest.getGender());
            personMap.put(personJsonRequest.getId(), person);
        }
        return personMap;
    }

    private void processPersonRelationships(String treeId, TreeImportJsonRequest importRequest, Map<String, Person> personMap) {
        for (PersonJsonRequest personJsonRequest : importRequest.getPersons()) {
            Person person = personMap.get(personJsonRequest.getId());
            for (PersonRelationshipRequest personRelationshipRequest : personJsonRequest.getRelationships()) {
                Person child = personMap.get(personRelationshipRequest.getChildId());
                if (nonNull(child)) {
                    personService.addParentChildRelationship(treeId, person.getId(), child.getId(), personRelationshipRequest.getRelationship().toString());
                }
            }
        }
    }

    private Map<String, Family> processFamilies(TreeImportJsonRequest importRequest, Tree tree, Map<String, Person> personMap, Map<String, Participant> participantMap, String userId) {
        Map<String, Family> familyMap = new HashMap<>();
        for (FamilyJsonRequest familyJsonRequest : importRequest.getFamilies()) {
            Family family = familyCreationService.createFamily(userId, tree, familyJsonRequest.getStatus());
            familyMap.put(familyJsonRequest.getId(), family);
            participantMap.put(familyJsonRequest.getId(), family);

            addChildrenToFamily(tree.getId(), family.getId(), familyJsonRequest, personMap, userId);
            addParentsToFamily(tree.getId(), family.getId(), familyJsonRequest, personMap, userId);
        }
        return familyMap;
    }

    private void addParentsToFamily(String treeId, String familyId, FamilyJsonRequest familyJsonRequest, Map<String, Person> personMap, String userId) {
        Person father = personMap.get(familyJsonRequest.getFatherId());
        Person mother = personMap.get(familyJsonRequest.getMotherId());

        if (nonNull(father)) {
            familyService.addFatherToFamily(userId, treeId, familyId, father.getId());
        }
        if (nonNull(mother)) {
            familyService.addMotherToFamily(userId, treeId, familyId, mother.getId());
        }
    }

    private void addChildrenToFamily(String treeId, String familyId, FamilyJsonRequest familyJsonRequest, Map<String, Person> personMap, String userId) {
        for (String childId : familyJsonRequest.getChildrenIds()) {
            Person child = personMap.get(childId);
            if (nonNull(child)) {
                familyService.addChildToFamily(userId, treeId, familyId, child.getId(), PersonRelationshipType.BIOLOGICAL.name(), PersonRelationshipType.BIOLOGICAL.name());
            }
        }
    }

    private Map<String, Source> processSources(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Source> sourceMap = new HashMap<>();
        for (SourceJsonRequest sourceJsonRequest : importRequest.getSources()) {
            Source source = sourceCreationService.createSource(userId, tree, sourceJsonRequest.getName());
            sourceMap.put(sourceJsonRequest.getId(), source);
        }
        return sourceMap;
    }

    private Map<String, File> processFiles(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, File> fileMap = new HashMap<>();
        for (FileJsonRequest fileJsonRequest : importRequest.getFiles()) {
            File file = fileCreationService.createFile(userId, tree, fileJsonRequest.getName(), fileJsonRequest.getType(), fileJsonRequest.getPath());
            fileMap.put(fileJsonRequest.getId(), file);
        }

        return fileMap;
    }

    private Map<String, Citation> processCitations(TreeImportJsonRequest importRequest, Tree tree, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        Map<String, Citation> citationMap = new HashMap<>();
        for (CitationJsonRequest citationJsonRequest : importRequest.getCitations()) {
            Source source = sourceMap.get(citationJsonRequest.getSourceId());
            List<String> fileIds = new ArrayList<>();
            for (String fileId : citationJsonRequest.getFilesIds()) {
                File file = fileMap.get(fileId);
                if (nonNull(file)) {
                    fileIds.add(file.getId());
                }
            }
            Citation citation = citationCreationService.createCitationWithSourceAndFiles(userId, tree, citationJsonRequest.getPage(), citationJsonRequest.getDate(), source.getId(), fileIds);
            citationMap.put(citationJsonRequest.getId(), citation);

        }
        return citationMap;
    }

    private Map<String, Event> processEvents(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Event> eventMap = new HashMap<>();
        for (EventJsonRequest eventJsonRequest : importRequest.getEvents()) {
            Event event = eventCreationService.createEvent(userId, tree, eventJsonRequest.getType(), eventJsonRequest.getPlace(), eventJsonRequest.getDescription(), eventJsonRequest.getDate());
            eventMap.put(eventJsonRequest.getId(), event);
        }
        return eventMap;
    }

    private void processEventRelationships(Tree tree, TreeImportJsonRequest importRequest, Map<String, Event> eventMap,
                                           Map<String, Participant> participantMap, Map<String, Citation> citationMap, String userId) {
        for (EventJsonRequest eventJsonRequest : importRequest.getEvents()) {
            Event event = eventMap.get(eventJsonRequest.getId());
            for (EventParticipantRequest eventParticipantRequest : eventJsonRequest.getParticipants()) {
                Participant participant = participantMap.get(eventParticipantRequest.getParticipantId());
                if (nonNull(participant)) {
                    eventService.addParticipantToEvent(userId, tree.getId(), event.getId(), participant.getId(), eventParticipantRequest.getRelationship().name());
                }
            }
            for (EventCitationRequest eventCitationRequest : eventJsonRequest.getCitations()) {
                Citation citation = citationMap.get(eventCitationRequest.getCitationId());
                if (nonNull(citation)) {
                    eventService.addCitationToEvent(userId, tree.getId(), event.getId(), citation.getId());
                }
            }
        }
    }
}