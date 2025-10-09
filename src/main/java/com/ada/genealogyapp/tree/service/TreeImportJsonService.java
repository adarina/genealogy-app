package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.dto.EventParticipantRequest;
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
import com.ada.genealogyapp.graphuser.service.GraphUserViewService;
import com.ada.genealogyapp.tree.dto.params.TreeImportJsonParams;
import com.ada.genealogyapp.location.dto.LocationJsonRequest;
import com.ada.genealogyapp.location.model.Location;
import com.ada.genealogyapp.location.service.LocationCreationService;
import com.ada.genealogyapp.location.service.LocationService;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.person.dto.PersonRelationshipRequest;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.dto.SourceJsonRequest;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.tree.dto.TreeImportJsonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static java.util.Objects.nonNull;

@Service

public class TreeImportJsonService extends TreeImportService<TreeImportJsonRequest, TreeImportJsonParams> {

    private final ObjectMapper objectMapper;
    private final PersonCreationService personCreationService;
    private final PersonService personService;
    private final FamilyCreationService familyCreationService;
    private final FamilyService familyService;
    private final SourceCreationService sourceCreationService;
    private final LocationCreationService locationCreationService;
    private final LocationService locationService;
    private final FileCreationService fileCreationService;
    private final EventCreationService eventCreationService;
    private final EventService eventService;
    private final CitationCreationService citationCreationService;
    private final CitationService citationService;


    public TreeImportJsonService(TreeCreationService treeCreationService, GraphUserViewService graphUserViewService,
                                 ObjectMapper objectMapper, PersonCreationService personCreationService,
                                 PersonService personService, FamilyCreationService familyCreationService,
                                 FamilyService familyService, SourceCreationService sourceCreationService,
                                 LocationCreationService locationCreationService, LocationService locationService,
                                 FileCreationService fileCreationService, EventCreationService eventCreationService,
                                 EventService eventService, CitationCreationService citationCreationService,
                                 CitationService citationService) {
        super(graphUserViewService, treeCreationService);
        this.objectMapper = objectMapper;
        this.personCreationService = personCreationService;
        this.personService = personService;
        this.familyCreationService = familyCreationService;
        this.familyService = familyService;
        this.sourceCreationService = sourceCreationService;
        this.locationCreationService = locationCreationService;
        this.locationService = locationService;
        this.fileCreationService = fileCreationService;
        this.eventCreationService = eventCreationService;
        this.eventService = eventService;
        this.citationCreationService = citationCreationService;
        this.citationService = citationService;
    }

    @Override
    protected TreeImportJsonRequest parseInputFile(InputStream inputStream) throws IOException {
        return objectMapper.readValue(inputStream, TreeImportJsonRequest.class);
    }

    @Override
    protected TreeImportJsonParams initializeImportParams(Tree tree, String userId) {
        return TreeImportJsonParams.builder()
                .tree(tree)
                .userId(userId)
                .build();
    }

    @Override
    protected void processEntities(TreeImportJsonRequest importRequest, TreeImportJsonParams params) {
        List<EntityProcessor<TreeImportJsonRequest, TreeImportJsonParams>> processors = Arrays.asList(
                this::processPersons,
                this::processFamilies,
                this::processSources,
                this::processLocations,
                this::processFiles,
                this::processCitations,
                this::processEvents,
                this::processPersonRelationships,
                this::processFamilyRelationships,
                this::processLocationRelationships,
                this::processCitationRelationships,
                this::processEventRelationships
        );
        processAllEntities(importRequest, params, processors);
    }

    @Override
    public void processPersons(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<PersonJsonRequest> personRequests = request.getPersons();
        if (personRequests == null || personRequests.isEmpty()) return;

        Map<String, Person> temporaryIds = personCreationService.createPersons(params.getUserId(), params.getTree().getId(), personRequests);
        params.getPersonMap().putAll(temporaryIds);
        params.getParticipantMap().putAll(temporaryIds);
    }

    @Override
    public void processFamilies(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<FamilyJsonRequest> familyRequests = request.getFamilies();
        if (familyRequests == null || familyRequests.isEmpty()) return;

        Map<String, Family> temporaryIds = familyCreationService.createFamilies(params.getUserId(), params.getTree().getId(), familyRequests);
        params.getFamilyMap().putAll(temporaryIds);
        params.getParticipantMap().putAll(temporaryIds);
    }

    @Override
    public void processSources(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<SourceJsonRequest> sourceRequests = request.getSources();
        if (sourceRequests == null || sourceRequests.isEmpty()) return;

        Map<String, Source> temporaryIds = sourceCreationService.createSources(params.getUserId(), params.getTree().getId(), sourceRequests);
        params.getSourceMap().putAll(temporaryIds);
    }

    public void processLocations(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<LocationJsonRequest> locationRequests = request.getLocations();
        if (locationRequests == null || locationRequests.isEmpty()) return;

        Map<String, Location> temporaryIds = locationCreationService.createLocations(params.getUserId(), params.getTree().getId(), locationRequests);
        params.getLocationMap().putAll(temporaryIds);
    }

    @Override
    public void processFiles(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<FileJsonRequest> fileRequests = request.getFiles();
        if (fileRequests == null || fileRequests.isEmpty()) return;

        Map<String, File> temporaryIds = fileCreationService.createFiles(params.getUserId(), params.getTree().getId(), fileRequests);
        params.getFileMap().putAll(temporaryIds);
    }

    public void processEvents(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<EventJsonRequest> eventRequests = request.getEvents();
        if (eventRequests == null || eventRequests.isEmpty()) return;

        Map<String, Event> temporaryIds = eventCreationService.createEvents(params.getUserId(), params.getTree().getId(), eventRequests);
        params.getEventMap().putAll(temporaryIds);
    }

    public void processCitations(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<CitationJsonRequest> citationRequests = request.getCitations();
        if (citationRequests == null || citationRequests.isEmpty()) return;

        Map<String, Citation> temporaryIds = citationCreationService.createCitations(params.getUserId(), params.getTree().getId(), citationRequests);
        params.getCitationMap().putAll(temporaryIds);
    }


    private void processPersonRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<PersonJsonRequest> personRequests = request.getPersons();
        if (personRequests == null || personRequests.isEmpty()) return;

        List<Map<String, Object>> relationshipsData = new ArrayList<>();
        for (PersonJsonRequest personRequest : personRequests) {
            Person parent = params.getPersonMap().get(personRequest.getId());
            if (nonNull(parent) && nonNull(personRequest.getRelationships())) {
                for (PersonRelationshipRequest relRequest : personRequest.getRelationships()) {
                    Person child = params.getPersonMap().get(relRequest.getChildId());
                    if (nonNull(child)) {
                        relationshipsData.add(Map.of(
                                "parentId", parent.getId(),
                                "childId", child.getId(),
                                "type", relRequest.getRelationship().name()
                        ));
                    }
                }
            }
        }
        personService.addParentChildRelationships(params.getTree().getId(), relationshipsData);
    }

    private void processFamilyRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<FamilyJsonRequest> familyRequests = request.getFamilies();
        if (familyRequests == null || familyRequests.isEmpty()) return;

        List<Map<String, Object>> fathersData = new ArrayList<>();
        List<Map<String, Object>> mothersData = new ArrayList<>();
        List<Map<String, Object>> childrenData = new ArrayList<>();

        for (FamilyJsonRequest familyRequest : familyRequests) {
            Family family = params.getFamilyMap().get(familyRequest.getId());

            if (nonNull(familyRequest.getFatherId())) {
                Person father = params.getPersonMap().get(familyRequest.getFatherId());
                if (nonNull(father)) fathersData.add(Map.of("familyId", family.getId(), "personId", father.getId()));
            }

            if (nonNull(familyRequest.getMotherId())) {
                Person mother = params.getPersonMap().get(familyRequest.getMotherId());
                if (nonNull(mother)) mothersData.add(Map.of("familyId", family.getId(), "personId", mother.getId()));
            }

            if (nonNull(familyRequest.getChildrenIds())) {
                for (String childTemporaryId : familyRequest.getChildrenIds()) {
                    Person child = params.getPersonMap().get(childTemporaryId);
                    if (nonNull(child)) {
                        childrenData.add(Map.of(
                                "familyId", family.getId(),
                                "personId", child.getId(),
                                "fatherRelationship", PersonRelationshipType.BIOLOGICAL.name(),
                                "motherRelationship", PersonRelationshipType.BIOLOGICAL.name()
                        ));
                    }
                }
            }
        }
        familyService.addFamilyRelationships(params.getUserId(), params.getTree().getId(), fathersData, mothersData, childrenData);
    }

    private void processLocationRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<LocationJsonRequest> locationRequests = request.getLocations();
        if (locationRequests == null || locationRequests.isEmpty()) return;

        List<Map<String, Object>> relationshipsData = new ArrayList<>();
        for (LocationJsonRequest locationRequest : locationRequests) {
            if (nonNull(locationRequest.getLocationId())) {
                Location child = params.getLocationMap().get(locationRequest.getId());
                Location parent = params.getLocationMap().get(locationRequest.getLocationId());
                if (nonNull(child) && nonNull(parent)) {
                    relationshipsData.add(Map.of("childId", child.getId(), "parentId", parent.getId()));
                }
            }
        }
        locationService.addLocatedInRelationships(params.getUserId(), params.getTree().getId(), relationshipsData);

    }

    private void processCitationRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<CitationJsonRequest> citationRequests = request.getCitations();
        if (citationRequests == null || citationRequests.isEmpty()) return;

        List<Map<String, String>> sourcesToAdd = new ArrayList<>();
        List<Map<String, String>> filesToAdd = new ArrayList<>();

        for (CitationJsonRequest citationJsonRequest : citationRequests) {
            Citation citation = params.getCitationMap().get(citationJsonRequest.getId());

            if (nonNull(citationJsonRequest.getSourceId())) {
                Source source = params.getSourceMap().get(citationJsonRequest.getSourceId());
                if (nonNull(source)) {
                    sourcesToAdd.add(Map.of("citationId", citation.getId(), "sourceId", source.getId()));
                }
            }
            if (nonNull(citationJsonRequest.getFilesIds())) {
                for (String fileRequest : citationJsonRequest.getFilesIds()) {
                    File file = params.getFileMap().get(fileRequest);
                    if (nonNull(file)) {
                        filesToAdd.add(Map.of("citationId", citation.getId(), "fileId", file.getId()));
                    }
                }
            }
        }
        citationService.addFilesToEvents(params.getUserId(), params.getTree().getId(), filesToAdd);
        citationService.addSourcesToEvents(params.getUserId(), params.getTree().getId(), sourcesToAdd);
//        citationService.addFilesAndSourcesToEvents(params.getUserId(), params.getTree().getId(), filesToAdd, sourcesToAdd);
    }


    private void processEventRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        List<EventJsonRequest> eventRequests = request.getEvents();
        if (eventRequests == null || eventRequests.isEmpty()) return;

        List<Map<String, Object>> participantsToAdd = new ArrayList<>();
        List<Map<String, String>> citationsToAdd = new ArrayList<>();
        List<Map<String, String>> locationsToAdd = new ArrayList<>();

        for (EventJsonRequest eventJsonRequest : eventRequests) {
            Event event = params.getEventMap().get(eventJsonRequest.getId());
            if (nonNull(eventJsonRequest.getParticipants())) {
                for (EventParticipantRequest participantRequest : eventJsonRequest.getParticipants()) {
                    Participant participant = params.getParticipantMap().get(participantRequest.getParticipantId());
                    if (nonNull(participant)) {
                        participantsToAdd.add(Map.of("eventId", event.getId(), "participantId", participant.getId(), "relationshipType", participantRequest.getRelationship().name()));
                    }
                }
            }
            if (nonNull(eventJsonRequest.getCitations())) {
                for (EventCitationRequest citationRequest : eventJsonRequest.getCitations()) {
                    Citation citation = params.getCitationMap().get(citationRequest.getCitationId());
                    if (nonNull(citation))
                        citationsToAdd.add(Map.of("eventId", event.getId(), "citationId", citation.getId()));
                }
            }
            if (nonNull(eventJsonRequest.getLocationId())) {
                Location location = params.getLocationMap().get(eventJsonRequest.getLocationId());
                if (nonNull(location))
                    locationsToAdd.add(Map.of("eventId", event.getId(), "locationId", location.getId()));
            }
        }
        eventService.addParticipantsToEvents(params.getUserId(), params.getTree().getId(), participantsToAdd);
        eventService.addCitationsToEvents(params.getUserId(), params.getTree().getId(), citationsToAdd);
        eventService.addLocationsToEvents(params.getUserId(), params.getTree().getId(), locationsToAdd);
    }
}

