package com.ada.genealogyapp.tree.service;


import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
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
import com.ada.genealogyapp.graphuser.model.GraphUser;
import com.ada.genealogyapp.graphuser.repository.GraphUserRepository;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.tree.dto.*;
import com.ada.genealogyapp.tree.dto.gedcom.*;
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

    private final TreeRepository treeRepository;

    private final PersonCreationService personCreationService;

    private final FamilyCreationService familyCreationService;

    private final EventCreationService eventCreationService;

    private final CitationCreationService citationCreationService;

    private final SourceCreationService sourceCreationService;

    private final FamilyService familyService;

    private final CitationService citationService;

    private final FileCreationService fileCreationService;

    private final GraphUserRepository graphUserRepository;

    @TransactionalInNeo4j
    public Tree importTree(TreeImportGedcomRequest importRequest) throws IOException {
        GraphUser graphUser = graphUserRepository.find("1");
        Tree tree = initializeTree(importRequest, graphUser);
        Map<String, Source> sourceMap = processSources(importRequest, tree, graphUser.getId());
        Map<String, File> fileMap = processFiles(importRequest, tree, graphUser.getId());
        Map<String, Person> personMap = processPersons(importRequest, tree, sourceMap, fileMap, graphUser.getId());
        Map<String, Participant> participantMap = new HashMap<>(personMap);
        Map<String, Family> familyMap = processFamilies(importRequest, tree, personMap, participantMap, sourceMap, fileMap, graphUser.getId());

        return tree;
    }

    private Tree initializeTree(TreeImportGedcomRequest importRequest, GraphUser graphUser) {
        Tree tree = Tree.builder()
                .graphUser(graphUser)
                .build();
        treeRepository.save(tree);
        return tree;
    }

    private Map<String, Person> processPersons(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        Map<String, Person> personMap = new HashMap<>();
        for (PersonGedcomRequest people : treeImportGedcomRequest.getPeople()) {
            Person person = personCreationService.createPerson(userId, tree,
                    people.getNames().get(0).getGivn(),
                    people.getNames().get(0).getSurn(),
                    GenderMapper.mapGender(people.getEventsFacts().get(0).getValue())
            );
            personMap.put(people.getId(), person);
            List<EventFact> eventsFacts = people.getEventsFacts();

            processEvents(eventsFacts, person, tree, EventParticipantRelationshipType.MAIN, sourceMap, fileMap, userId);
        }
        return personMap;
    }

    private Map<String, Family> processFamilies(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Person> personMap, Map<String, Participant> participantMap, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        Map<String, Family> familyMap = new HashMap<>();
        for (FamilyGedcomRequest families : treeImportGedcomRequest.getFamilies()) {
            Family family = familyCreationService.createFamily(userId, tree, StatusType.MARRIED);

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
                familyService.addFatherToFamily(userId, treeId, familyId, parent.getId());
            } else {
                familyService.addMotherToFamily(userId, treeId, familyId, parent.getId());
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
                    familyService.addChildToFamily(userId, treeId, familyId, child.getId(), PersonRelationshipType.BIOLOGICAL.name(), PersonRelationshipType.BIOLOGICAL.name());
                }
            }
        }
    }

    private Map<String, Source> processSources(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, String userId) {
        Map<String, Source> sourceMap = new HashMap<>();
        for (SourceGedcomRequest sourceGedcomRequest : treeImportGedcomRequest.getSources()) {
            Source source = sourceCreationService.createSource(userId, tree, sourceGedcomRequest.getTitl());
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

                    Citation citation = citationCreationService.createCitationWithSourceAndEvent(userId, tree, sourceCitation.getPage(), sourceCitation.getDate(), sourceId, eventId);
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
                citationService.addFileToCitation(userId, treeId, citationId, file.getId());
            }
        }
    }

    private void processEvents(List<EventFact> eventsFacts, Participant participant, Tree tree, EventParticipantRelationshipType relationship, Map<String, Source> sourceMap, Map<String, File> fileMap, String userId) {
        if (nonNull(eventsFacts)) {
            for (EventFact eventFact : eventsFacts) {
                if (nonNull(eventFact)) {
                    EventType type = EventMapper.mapEvent(eventFact.getTag());
                    if (!type.equals(EventType.ERROR)) {
                        Event event = eventCreationService.createEventWithParticipant(userId, tree, type, eventFact.getPlace(), eventFact.getType(), eventFact.getDate(), participant, relationship);

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

            File file = fileCreationService.createFile(userId, tree, name, type, path);

            fileMap.put(filesRequestJson.getId(), file);
        }
        return fileMap;
    }
}
