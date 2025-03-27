package com.ada.genealogyapp.tree.service;


import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.dto.params.CreateCitationWithSourceAndEventParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.dto.params.CreateFileRequestParams;
import com.ada.genealogyapp.file.service.FileCreationService;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.gedcom.dto.EventFact;
import com.ada.genealogyapp.gedcom.dto.MediaRef;
import com.ada.genealogyapp.gedcom.dto.Reference;
import com.ada.genealogyapp.gedcom.dto.SourceCitation;
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.transaction.TransactionalInNeo4j;
import com.ada.genealogyapp.tree.dto.*;
import com.ada.genealogyapp.tree.dto.gedcom.*;
import com.ada.genealogyapp.gedcom.mappers.EventMapper;
import com.ada.genealogyapp.gedcom.mappers.GenderMapper;
import com.ada.genealogyapp.tree.dto.params.CreateTreeImportParams;
import com.ada.genealogyapp.tree.dto.params.CreateTreeRequestParams;
import com.ada.genealogyapp.tree.model.Tree;
import com.ada.genealogyapp.tree.repository.TreeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.nonNull;

@Service
@RequiredArgsConstructor
public class TreeImportGedcomService {

    private final PersonCreationService personCreationService;

    private final FamilyCreationService familyCreationService;

    private final EventCreationService eventCreationService;

    private final CitationCreationService citationCreationService;

    private final SourceCreationService sourceCreationService;

    private final FamilyService familyService;

    private final CitationService citationService;

    private final FileCreationService fileCreationService;

    private final GraphUserRepository graphUserRepository;

    private final TreeCreationService treeCreationService;

    @TransactionalInNeo4j
    public Tree importTree(TreeImportGedcomRequest importRequest) throws IOException {
        GraphUser graphUser = graphUserRepository.find("1");
        Tree tree = treeCreationService.createTreeImport(CreateTreeImportParams.builder()
                .userId(graphUser.getId()).name("sth").build());
        Map<String, Source> sourceMap = processSources(importRequest, tree, graphUser.getId());
        Map<String, File> fileMap = processFiles(importRequest, tree, graphUser.getId());
        Map<String, Person> personMap = processPersons(importRequest, tree, sourceMap, fileMap, graphUser.getId());
        Map<String, Participant> participantMap = new HashMap<>(personMap);
        Map<String, Family> familyMap = processFamilies(importRequest, tree, personMap, participantMap, sourceMap, fileMap, graphUser.getId());

        return tree;
    }

    private Map<String, Person> processPersons(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        Map<String, Person> personMap = new HashMap<>();
        for (PersonGedcomRequest people : treeImportGedcomRequest.getPeople()) {
            PersonRequest personRequest = PersonRequest.builder()
                    .firstname(people.getNames().get(0).getGivn())
                    .lastname(people.getNames().get(0).getSurn())
                    .gender(GenderMapper.mapGender(people.getEventsFacts().get(0).getValue()))
                    .build();
            Person person = personCreationService.createPerson(CreatePersonRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .personRequest(personRequest)
                    .build());
            personMap.put(people.getId(), person);
            List<EventFact> eventsFacts = people.getEventsFacts();

            processEvents(eventsFacts, person, tree, EventParticipantRelationshipType.MAIN, sourceMap, fileMap, userId);
        }
        return personMap;
    }

    private Map<String, Family> processFamilies(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Person> personMap, Map<String, Participant> participantMap, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        Map<String, Family> familyMap = new HashMap<>();
        for (FamilyGedcomRequest families : treeImportGedcomRequest.getFamilies()) {
            FamilyRequest familyRequest = FamilyRequest.builder()
                    .status(StatusType.MARRIED)
                    .build();
            Family family = familyCreationService.createFamily(CreateFamilyRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .familyRequest(familyRequest)
                    .build());

            addChildrenToFamily(userId, tree.getId(), family.getId(), families, personMap);
            addParentsToFamily(userId, tree.getId(), family.getId(), families, personMap);

            familyMap.put(families.getId(), family);
            participantMap.put(families.getId(), family);

            List<EventFact> eventsFacts = families.getEventsFacts();
            processEvents(eventsFacts, family, tree, EventParticipantRelationshipType.FAMILY, sourceMap, fileMap, userId);
        }
        return familyMap;
    }

    public void addParentsToFamily(String userId, String treeId, String familyId, FamilyGedcomRequest familyGedcomRequest, Map<String, Person> personMap) {
        addParentToFamily(userId, treeId, familyId, familyGedcomRequest.getHusbandRefs(), personMap, true);
        addParentToFamily(userId, treeId, familyId, familyGedcomRequest.getWifeRefs(), personMap, false);

    }

    private void addParentToFamily(String userId, String treeId, String familyId, List<Reference> parentRefs, Map<String, Person> personMap, boolean isFather) {
        String parentRef = (nonNull(parentRefs)) ? parentRefs.get(0).getRef() : null;
        Person parent = personMap.get(parentRef);

        if (nonNull(parent)) {
            if (isFather) {
                familyService.addFatherToFamily(AddPersonToFamilyParams.builder()
                        .userId(userId)
                        .treeId(treeId)
                        .familyId(familyId)
                        .personId(parent.getId())
                        .build());
            } else {
                familyService.addMotherToFamily(AddPersonToFamilyParams.builder()
                        .userId(userId)
                        .treeId(treeId)
                        .familyId(familyId)
                        .personId(parent.getId())
                        .build());
            }
        }
    }

    public void addChildrenToFamily(String userId, String treeId, String familyId, FamilyGedcomRequest familyGedcomRequest, Map<String, Person> personMap) {
        List<Reference> childRefs = familyGedcomRequest.getChildRefs();
        if (nonNull(childRefs)) {
            for (Reference childRef : familyGedcomRequest.getChildRefs()) {
                String childId = childRef.getRef();
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
    }

    private Map<String, Source> processSources(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, String userId) {
        Map<String, Source> sourceMap = new HashMap<>();
        for (SourceGedcomRequest sourceGedcomRequest : treeImportGedcomRequest.getSources()) {
            SourceRequest sourceRequest = SourceRequest.builder()
                    .name(sourceGedcomRequest.getTitl())
                    .build();
            Source source = sourceCreationService.createSource(CreateSourceRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .sourceRequest(sourceRequest)
                    .build());

            sourceMap.put(sourceGedcomRequest.getId(), source);
        }
        return sourceMap;
    }

    private void processCitations(Tree tree, String eventId, List<SourceCitation> sourceCitations, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        if (nonNull(sourceCitations)) {
            for (SourceCitation sourceCitation : sourceCitations) {
                String sourceRef = sourceCitation.getRef();
                Source source = (nonNull(sourceRef)) ? sourceMap.get(sourceRef) : null;

                if (nonNull(source)) {
                    String sourceId = source.getId();
                    CitationRequest citationRequest = CitationRequest.builder()
                            .page(sourceCitation.getPage())
                            .date(sourceCitation.getDate())
                            .build();

                    Citation citation = citationCreationService.createCitationWithSourceAndEvent(CreateCitationWithSourceAndEventParams.builder()
                            .userId(userId)
                            .treeId(tree.getId())
                            .citationRequest(citationRequest)
                            .sourceId(sourceId)
                            .eventId(eventId)
                            .build());
                    List<MediaRef> mediaRef = sourceCitation.getMediaRefs();
                    processFiles(tree.getId(), citation.getId(), mediaRef, fileMap, userId);
                }
            }
        }
    }

    private void processFiles(String treeId, String citationId, List<MediaRef> mediaRef, Map<String, File> fileMap, String userId) {
        for (MediaRef ref : mediaRef) {
            String fileRef = ref.getRef();
            File file = (nonNull(fileRef)) ? fileMap.get(fileRef) : null;

            if (nonNull(file)) {
                citationService.addFileToCitation(AddFileToCitationParams.builder()
                        .userId(userId)
                        .treeId(treeId)
                        .citationId(citationId)
                        .fileId(file.getId())
                        .build());
            }
        }
    }

    private void processEvents(List<EventFact> eventsFacts, Participant participant, Tree tree, EventParticipantRelationshipType relationship, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        if (nonNull(eventsFacts)) {
            for (EventFact eventFact : eventsFacts) {
                if (nonNull(eventFact)) {
                    EventType type = EventMapper.mapEvent(eventFact.getTag());
                    if (!type.equals(EventType.ERROR)) {
                        EventRequest eventRequest = EventRequest.builder()
                                .place(eventFact.getPlace())
                                .type(type)
                                .date(eventFact.getDate())
                                .description(eventFact.getType())
                                .build();
                        Event event = eventCreationService.createEventWithParticipant(CreateEventRequestWithParticipantParams.builder()
                                .userId(userId)
                                .treeId(tree.getId())
                                .eventRequest(eventRequest)
                                .participantId(participant.getId())
                                .relationshipType(relationship.name())
                                .build());

                        List<SourceCitation> sourceCitations = eventFact.getSourceCitations();
                        processCitations(tree, event.getId(), sourceCitations, sourceMap, fileMap, userId);
                    }
                }
            }
        }
    }


    private Map<String, File> processFiles(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, String userId) throws IOException {
        Map<String, File> fileMap = new HashMap<>();
        for (FileGedcomRequest filesRequestJson : treeImportGedcomRequest.getMedia()) {

            String type = filesRequestJson.getExtensions().getMoreTags().get(0).getValue();
            String name = filesRequestJson.getExtensions().getMoreTags().get(1).getValue() + "." + type;
            String path = filesRequestJson.get_file() + "." + type;
            type = Files.probeContentType(Path.of(path));
            FileRequest fileRequest = FileRequest.builder()
                    .path(path)
                    .type(type)
                    .name(name)
                    .build();

            File file = fileCreationService.createFile(CreateFileRequestParams.builder()
                    .userId(userId)
                    .treeId(tree.getId())
                    .fileRequest(fileRequest)
                    .build());

            fileMap.put(filesRequestJson.getId(), file);
        }
        return fileMap;
    }
}


