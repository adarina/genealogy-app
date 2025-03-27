package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.citation.dto.params.CreateCitationWithSourceAndFilesParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.dto.EventParticipantRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.dto.params.AddParticipantToEventParams;
import com.ada.genealogyapp.event.dto.params.CreateEventRequestParams;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.dto.FamilyJsonRequest;
import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.file.dto.FileJsonRequest;
import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.dto.params.CreateFileRequestParams;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileCreationService;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonRelationshipRequest;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.person.dto.params.AddParentChildRelationshipParams;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.dto.SourceJsonRequest;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.dto.TreeImportJsonRequest;
import com.ada.genealogyapp.tree.dto.params.CreateTreeImportParams;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Objects.nonNull;


@Service
@RequiredArgsConstructor
public class TreeImportJsonService {


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

    private final TreeCreationService treeCreationService;

    //TODO gdzies dublują się eventy dla osób o tym samym imieniu i nazwisku (chrzest i narodziny, pogrzeb i smierc ok)
    @TransactionalInNeo4j
    public Tree importTree(TreeImportJsonRequest importRequest) {
//        graphUserRepository.save("1");
        GraphUser graphUser = graphUserRepository.find("1");
        Tree tree = treeCreationService.createTreeImport(CreateTreeImportParams.builder()
                .userId(graphUser.getId()).name(importRequest.getTree().getName()).build());
        Map<String, Person> personMap = processPersons(importRequest, tree, graphUser.getId());
        Map<String, Participant> participantMap = new HashMap<>(personMap);
        processPersonRelationships(graphUser.getId(), tree.getId(), importRequest, personMap);

        Map<String, Family> familyMap = processFamilies(importRequest, tree, personMap, participantMap, graphUser.getId());

        Map<String, Source> sourceMap = processSources(importRequest, tree, graphUser.getId());

        Map<String, File> fileMap = processFiles(importRequest, tree, graphUser.getId());

        Map<String, Citation> citationMap = processCitations(importRequest, tree, sourceMap, fileMap, graphUser.getId());

        Map<String, Event> eventMap = processEvents(importRequest, tree, graphUser.getId());
        processEventRelationships(tree, importRequest, eventMap, participantMap, citationMap, graphUser.getId());

        return tree;
    }


    private Map<String, Person> processPersons(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Person> personMap = new HashMap<>();
        for (PersonJsonRequest personJsonRequest : importRequest.getPersons()) {
            Person person = personCreationService.createPerson(CreatePersonRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .personRequest(personJsonRequest)
                    .build());
            personMap.put(personJsonRequest.getId(), person);
        }
        return personMap;
    }

    private void processPersonRelationships(String userId, String treeId, TreeImportJsonRequest importRequest, Map<String, Person> personMap) {
        for (PersonJsonRequest personJsonRequest : importRequest.getPersons()) {
            Person person = personMap.get(personJsonRequest.getId());
            for (PersonRelationshipRequest personRelationshipRequest : personJsonRequest.getRelationships()) {
                Person child = personMap.get(personRelationshipRequest.getChildId());
                if (nonNull(child)) {
                    personService.addParentChildRelationship(AddParentChildRelationshipParams.builder()
                            .userId(userId)
                            .treeId(treeId)
                            .parentId(person.getId())
                            .childId(child.getId())
                            .relationshipType(personRelationshipRequest.getRelationship().toString())
                            .build());
                }
            }
        }
    }

    private Map<String, Family> processFamilies(TreeImportJsonRequest importRequest, Tree tree, Map<String, Person> personMap, Map<String, Participant> participantMap, String userId) {
        Map<String, Family> familyMap = new HashMap<>();
        for (FamilyJsonRequest familyJsonRequest : importRequest.getFamilies()) {
            Family family = familyCreationService.createFamily(CreateFamilyRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .familyRequest(familyJsonRequest)
                    .build());
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
            familyService.addFatherToFamily(AddPersonToFamilyParams.builder()
                    .userId(userId)
                    .treeId(treeId)
                    .familyId(familyId)
                    .personId(father.getId())
                    .build());
        }
        if (nonNull(mother)) {
            familyService.addMotherToFamily(AddPersonToFamilyParams.builder()
                    .userId(userId)
                    .treeId(treeId)
                    .familyId(familyId)
                    .personId(mother.getId())
                    .build());
        }
    }

    private void addChildrenToFamily(String treeId, String familyId, FamilyJsonRequest familyJsonRequest, Map<String, Person> personMap, String userId) {
        for (String childId : familyJsonRequest.getChildrenIds()) {
            Person child = personMap.get(childId);
            if (nonNull(child)) {
                familyService.addChildToFamily(AddChildToFamilyRequestParams.builder()
                        .userId(userId)
                        .treeId(treeId)
                        .familyId(familyId)
                        .personId(child.getId())
                        .familyChildRequest(FamilyChildRequest.builder()
                                .fatherRelationship(PersonRelationshipType.BIOLOGICAL)
                                .motherRelationship(PersonRelationshipType.BIOLOGICAL)
                                .build())
                        .build());
            }
        }
    }

    private Map<String, Source> processSources(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Source> sourceMap = new HashMap<>();
        for (SourceJsonRequest sourceJsonRequest : importRequest.getSources()) {
            Source source = sourceCreationService.createSource(CreateSourceRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .sourceRequest(sourceJsonRequest)
                    .build());
            sourceMap.put(sourceJsonRequest.getId(), source);
        }
        return sourceMap;
    }

    private Map<String, File> processFiles(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, File> fileMap = new HashMap<>();
        for (FileJsonRequest fileJsonRequest : importRequest.getFiles()) {

            FileRequest fileRequest = FileRequest.builder()
                    .path(fileJsonRequest.getPath())
                    .type(fileJsonRequest.getType())
                    .name(fileJsonRequest.getName())
                    .build();
            File file = fileCreationService.createFile(CreateFileRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .fileRequest(fileRequest)
                    .build());
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

            Citation citation = citationCreationService.createCitationWithSourceAndFiles(CreateCitationWithSourceAndFilesParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .citationRequest(citationJsonRequest)
                    .sourceId(source.getId())
                    .filesIds(fileIds)
                    .build());
            citationMap.put(citationJsonRequest.getId(), citation);

        }
        return citationMap;
    }

    private Map<String, Event> processEvents(TreeImportJsonRequest importRequest, Tree tree, String userId) {
        Map<String, Event> eventMap = new HashMap<>();
        for (EventJsonRequest eventJsonRequest : importRequest.getEvents()) {
            EventRequest eventRequest = EventRequest.builder()
                    .place(eventJsonRequest.getPlace())
                    .date(eventJsonRequest.getDate())
                    .type(eventJsonRequest.getType())
                    .description(eventJsonRequest.getDescription())
                    .build();
            Event event = eventCreationService.createEvent(CreateEventRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .eventRequest(eventRequest)
                    .build());
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
                    eventService.addParticipantToEvent(AddParticipantToEventParams.builder()
                            .userId(userId)
                            .treeId(tree.getId())
                            .eventId(event.getId())
                            .participantId(participant.getId())
                            .relationshipType(eventParticipantRequest.getRelationship().name())
                            .build());
                }
            }
            for (EventCitationRequest eventCitationRequest : eventJsonRequest.getCitations()) {
                Citation citation = citationMap.get(eventCitationRequest.getCitationId());
                if (nonNull(citation)) {
                    eventService.addCitationToEvent(AddCitationToEventParams.builder()
                            .userId(userId)
                            .treeId(tree.getId())
                            .eventId(event.getId())
                            .citationId(citation.getId())
                            .build());
                }
            }
        }
    }
}