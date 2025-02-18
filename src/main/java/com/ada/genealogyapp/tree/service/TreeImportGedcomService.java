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
import com.ada.genealogyapp.file.service.FileService;
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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.Neo4jClient;
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

    @TransactionalInNeo4j
    public Tree importTree(TreeImportGedcomRequest importRequest) throws IOException {

        Tree tree = initializeTree(importRequest);
        Map<String, Source> sourceMap = processSources(importRequest, tree);
        Map<String, File> fileMap = processFiles(importRequest, tree);
        Map<String, Person> personMap = processPersons(importRequest, tree, sourceMap, fileMap);
        Map<String, Participant> participantMap = new HashMap<>(personMap);
        Map<String, Family> familyMap = processFamilies(importRequest, tree, personMap, participantMap, sourceMap, fileMap);

        return tree;
    }

    private Tree initializeTree(TreeImportGedcomRequest importRequest) {
        Tree tree = Tree.builder()
//                .name(importRequest.getTree().getName())
//                .userId(importRequest.getTree().getUserId())
                .build();
        treeRepository.save(tree);
        return tree;
    }

    private Map<String, Person> processPersons(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Source> sourceMap, Map<String, File> fileMap) {
        Map<String, Person> personMap = new HashMap<>();
        for (PersonGedcomRequest people : treeImportGedcomRequest.getPeople()) {
            Person person = personCreationService.createPerson(tree,
                    people.getNames().get(0).getGivn(),
                    people.getNames().get(0).getSurn(),
                    GenderMapper.mapGender(people.getEventsFacts().get(0).getValue())
            );
            personMap.put(people.getId(), person);
            List<EventFact> eventsFacts = people.getEventsFacts();

            processEvents(eventsFacts, person, tree, EventParticipantRelationshipType.MAIN, sourceMap, fileMap);
        }
        return personMap;
    }

    private Map<String, Family> processFamilies(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree, Map<String, Person> personMap, Map<String, Participant> participantMap, Map<String, Source> sourceMap, Map<String, File> fileMap) {
        Map<String, Family> familyMap = new HashMap<>();
        for (FamilyGedcomRequest families : treeImportGedcomRequest.getFamilies()) {
            Family family = familyCreationService.createFamily(tree, StatusType.MARRIED);

            addChildrenToFamily(tree.getId(), family.getId(), families, personMap);
            addParentsToFamily(tree.getId(), family.getId(), families, personMap);

            familyMap.put(families.getId(), family);
            participantMap.put(families.getId(), family);

            List<EventFact> eventsFacts = families.getEventsFacts();
            processEvents(eventsFacts, family, tree, EventParticipantRelationshipType.FAMILY, sourceMap, fileMap);
        }
        return familyMap;
    }

    public void addParentsToFamily(String treeId, String familyId, FamilyGedcomRequest familyGedcomRequest, Map<String, Person> personMap) {
        addParentToFamily(treeId, familyId, familyGedcomRequest.getHusbandRefs(), personMap, true);
        addParentToFamily(treeId, familyId, familyGedcomRequest.getWifeRefs(), personMap, false);

    }

    private void addParentToFamily(String treeId, String familyId, List<Reference> parentRefs, Map<String, Person> personMap, boolean isFather) {
        String parentRef = (nonNull(parentRefs)) ? parentRefs.get(0).getRef() : null;
        Person parent = personMap.get(parentRef);

        if (nonNull(parent)) {
            if (isFather) {
                familyService.addFatherToFamily(treeId, familyId, parent.getId());
            } else {
                familyService.addMotherToFamily(treeId, familyId, parent.getId());
            }
//            familyService.addParentChildRelationship(treeId, familyId, parent.getId(), PersonRelationshipType.BIOLOGICAL.toString());
        }
    }

    public void addChildrenToFamily(String treeId, String familyId, FamilyGedcomRequest familyGedcomRequest, Map<String, Person> personMap) {
        List<Reference> childRefs = familyGedcomRequest.getChildRefs();
        if (nonNull(childRefs)) {
            for (Reference childRef : familyGedcomRequest.getChildRefs()) {
                String childId = childRef.getRef();
                Person child = personMap.get(childId);

                if (nonNull(child)) {
                    familyService.addChildToFamily(treeId, familyId, child.getId(), PersonRelationshipType.BIOLOGICAL.name(), PersonRelationshipType.BIOLOGICAL.name());
                }
            }
        }
    }

    private Map<String, Source> processSources(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree) {
        Map<String, Source> sourceMap = new HashMap<>();
        for (SourceGedcomRequest sourceGedcomRequest : treeImportGedcomRequest.getSources()) {
            Source source = sourceCreationService.createSource(tree, sourceGedcomRequest.getTitl());
            sourceMap.put(sourceGedcomRequest.getId(), source);
        }
        return sourceMap;
    }

    private void processCitations(Tree tree, String eventId, List<SourceCitation> sourceCitations, Map<String, Source> sourceMap, Map<String, File> fileMap) {
        if (nonNull(sourceCitations)) {
            for (SourceCitation sourceCitation : sourceCitations) {
                String sourceRef = sourceCitation.getRef();
                Source source = (nonNull(sourceRef)) ? sourceMap.get(sourceRef) : null;

                if (nonNull(source)) {
                    String sourceId = source.getId();

                    Citation citation = citationCreationService.createCitationWithSourceAndEvent(tree, sourceCitation.getPage(), sourceCitation.getDate(), sourceId, eventId);
                    List<MediaRef> mediaRef = sourceCitation.getMediaRefs();
                    processFiles(tree.getId(), citation.getId(), mediaRef, fileMap);
                }
            }
        }
    }

    private void processFiles(String treeId, String citationId, List<MediaRef> mediaRef, Map<String, File> fileMap) {
        for (MediaRef ref : mediaRef) {
            String fileRef = ref.getRef();
            File file = (nonNull(fileRef)) ? fileMap.get(fileRef) : null;

            if (nonNull(file)) {
                citationService.addFileToCitation(treeId, citationId, file.getId());
            }
        }
    }

    private void processEvents(List<EventFact> eventsFacts, Participant participant, Tree tree, EventParticipantRelationshipType relationship, Map<String, Source> sourceMap, Map<String, File> fileMap) {
        if (nonNull(eventsFacts)) {
            for (EventFact eventFact : eventsFacts) {
                if (nonNull(eventFact)) {
                    EventType type = EventMapper.mapEvent(eventFact.getTag());
                    if (!type.equals(EventType.ERROR)) {
                        Event event = eventCreationService.createEventWithParticipant(tree, type, eventFact.getPlace(), eventFact.getType(), eventFact.getDate(), participant, relationship);

                        List<SourceCitation> sourceCitations = eventFact.getSourceCitations();
                        processCitations(tree, event.getId(), sourceCitations, sourceMap, fileMap);
                    }
                }
            }
        }
    }


    private Map<String, File> processFiles(TreeImportGedcomRequest treeImportGedcomRequest, Tree tree) throws IOException {
        Map<String, File> fileMap = new HashMap<>();
        for (FileGedcomRequest filesRequestJson : treeImportGedcomRequest.getMedia()) {

            String type = filesRequestJson.getExtensions().getMoreTags().get(0).getValue();
            String name = filesRequestJson.getExtensions().getMoreTags().get(1).getValue() + "." + type;
            String path = filesRequestJson.get_file() + "." + type;
            type = Files.probeContentType(Path.of(path));

            File file = fileCreationService.createFile(tree, name, type, path);

            fileMap.put(filesRequestJson.getId(), file);
        }
        return fileMap;
    }
}
