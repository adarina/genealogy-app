package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.citation.dto.params.CreateCitationWithSourceAndFilesParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.event.dto.EventCitationRequest;
import com.ada.genealogyapp.event.dto.EventJsonRequest;
import com.ada.genealogyapp.event.dto.EventParticipantRequest;
import com.ada.genealogyapp.event.dto.EventRequest;
import com.ada.genealogyapp.event.dto.params.AddCitationToEventParams;
import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
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
import com.ada.genealogyapp.graphuser.service.GraphUserViewService;
import com.ada.genealogyapp.tree.dto.params.TreeImportJsonParams;
import com.ada.genealogyapp.location.dto.LocationJsonRequest;
import com.ada.genealogyapp.location.dto.params.AddParentToLocationParams;
import com.ada.genealogyapp.location.dto.params.CreateLocationRequestParams;
import com.ada.genealogyapp.location.model.Location;
import com.ada.genealogyapp.location.service.LocationCreationService;
import com.ada.genealogyapp.location.service.LocationService;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonJsonRequest;
import com.ada.genealogyapp.person.dto.PersonRelationshipRequest;
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
import com.ada.genealogyapp.tree.dto.TreeImportJsonRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    public TreeImportJsonService(TreeCreationService treeCreationService, GraphUserViewService graphUserViewService,
                                 ObjectMapper objectMapper, PersonCreationService personCreationService,
                                 PersonService personService, FamilyCreationService familyCreationService,
                                 FamilyService familyService, SourceCreationService sourceCreationService,
                                 LocationCreationService locationCreationService, LocationService locationService,
                                 FileCreationService fileCreationService, EventCreationService eventCreationService, EventService eventService, CitationCreationService citationCreationService) {
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
                this::processEvents
        );
        processAllEntities(importRequest, params, processors);
    }

    @Override
    public void processPersons(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (PersonJsonRequest personJsonRequest : request.getPersons()) {
            Person person = personCreationService.createPerson(CreatePersonRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .personRequest(personJsonRequest)
                    .build());
            params.getPersonMap().put(personJsonRequest.getId(), person);
        }
        for (PersonJsonRequest personJsonRequest : request.getPersons()) {
            Person person = params.getPersonMap().get(personJsonRequest.getId());
            for (PersonRelationshipRequest personRelationshipRequest : personJsonRequest.getRelationships()) {
                Person child = params.getPersonMap().get(personRelationshipRequest.getChildId());
                if (nonNull(child)) {
                    personService.addParentChildRelationship(AddParentChildRelationshipParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .parentId(person.getId())
                            .childId(child.getId())
                            .relationshipType(personRelationshipRequest.getRelationship().toString())
                            .build());
                }
            }
        }
        params.getParticipantMap().putAll(params.getPersonMap());
    }


    @Override
    public void processFamilies(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (FamilyJsonRequest familyJsonRequest : request.getFamilies()) {
            Family family = familyCreationService.createFamily(CreateFamilyRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .familyRequest(familyJsonRequest)
                    .build());
            params.getFamilyMap().put(familyJsonRequest.getId(), family);
            params.getParticipantMap().put(familyJsonRequest.getId(), family);

            Person father = params.getPersonMap().get(familyJsonRequest.getFatherId());
            Person mother = params.getPersonMap().get(familyJsonRequest.getMotherId());
            if (nonNull(father)) {
                familyService.addFatherToFamily(AddPersonToFamilyParams.builder()
                        .userId(params.getUserId())
                        .treeId(params.getTree().getId())
                        .familyId(family.getId())
                        .personId(father.getId())
                        .build());
            }
            if (nonNull(mother)) {
                familyService.addMotherToFamily(AddPersonToFamilyParams.builder()
                        .userId(params.getUserId())
                        .treeId(params.getTree().getId())
                        .familyId(family.getId())
                        .personId(mother.getId())
                        .build());
            }
            for (String childId : familyJsonRequest.getChildrenIds()) {
                Person child = params.getPersonMap().get(childId);
                if (nonNull(child)) {
                    familyService.addChildToFamily(AddChildToFamilyRequestParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .familyId(family.getId())
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

    @Override
    public void processSources(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (SourceJsonRequest sourceJsonRequest : request.getSources()) {
            Source source = sourceCreationService.createSource(CreateSourceRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .sourceRequest(sourceJsonRequest)
                    .build());
            params.getSourceMap().put(sourceJsonRequest.getId(), source);
        }
    }

    private void processLocations(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (LocationJsonRequest locationJsonRequest : request.getLocations()) {
            Location location = locationCreationService.createLocation(CreateLocationRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .locationRequest(locationJsonRequest)
                    .build());
            params.getLocationMap().put(locationJsonRequest.getId(), location);
        }
        for (LocationJsonRequest locationJsonRequest : request.getLocations()) {
            if (nonNull(locationJsonRequest.getLocationId()) && params.getLocationMap().containsKey(locationJsonRequest.getId()) && params.getLocationMap().containsKey(locationJsonRequest.getLocationId())) {
                Location location = params.getLocationMap().get(locationJsonRequest.getId());
                Location parent = params.getLocationMap().get(locationJsonRequest.getLocationId());
                locationService.addParentToLocation(AddParentToLocationParams.builder()
                        .locationId(location.getId())
                        .parentId(parent.getId())
                        .userId(params.getUserId())
                        .treeId(params.getTree().getId())
                        .build());
            }
        }
    }

    @Override
    public void processFiles(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (FileJsonRequest fileJsonRequest : request.getFiles()) {
            File file = fileCreationService.createFile(CreateFileRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .fileRequest(FileRequest.builder()
                            .path(fileJsonRequest.getPath())
                            .type(fileJsonRequest.getType())
                            .name(fileJsonRequest.getName())
                            .build())
                    .build());
            params.getFileMap().put(fileJsonRequest.getId(), file);
        }
    }

    public void processEvents(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (EventJsonRequest eventJsonRequest : request.getEvents()) {
            Event event = eventCreationService.createEvent(CreateEventRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .eventRequest(EventRequest.builder()
                            .place(eventJsonRequest.getPlace())
                            .date(eventJsonRequest.getDate())
                            .type(eventJsonRequest.getType())
                            .description(eventJsonRequest.getDescription())
                            .build())
                    .build());
            params.getEventMap().put(eventJsonRequest.getId(), event);
        }
        processEventRelationships(request, params);
    }

    private void processEventRelationships(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (EventJsonRequest eventJsonRequest : request.getEvents()) {
            Event event = params.getEventMap().get(eventJsonRequest.getId());
            for (EventParticipantRequest eventParticipantRequest : eventJsonRequest.getParticipants()) {
                Participant participant = params.getParticipantMap().get(eventParticipantRequest.getParticipantId());
                if (nonNull(participant)) {
                    eventService.addParticipantToEvent(AddParticipantToEventParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .eventId(event.getId())
                            .participantId(participant.getId())
                            .relationshipType(eventParticipantRequest.getRelationship().name())
                            .build());
                }
            }
            for (EventCitationRequest eventCitationRequest : eventJsonRequest.getCitations()) {
                Citation citation = params.getCitationMap().get(eventCitationRequest.getCitationId());
                if (nonNull(citation)) {
                    eventService.addCitationToEvent(AddCitationToEventParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .eventId(event.getId())
                            .citationId(citation.getId())
                            .build());
                }
            }
            if (nonNull(eventJsonRequest.getLocationId())) {
                Location location = params.getLocationMap().get(eventJsonRequest.getLocationId());
                if (nonNull(location)) {
                    eventService.addLocationToEvent(AddLocationToEventParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .eventId(event.getId())
                            .locationId(location.getId())
                            .build());
                }
            }
        }
    }

    public void processCitations(TreeImportJsonRequest request, TreeImportJsonParams params) {
        for (CitationJsonRequest citationJsonRequest : request.getCitations()) {
            Source source = params.getSourceMap().get(citationJsonRequest.getSourceId());
            List<String> fileIds = new ArrayList<>();
            for (String fileId : citationJsonRequest.getFilesIds()) {
                File file = params.getFileMap().get(fileId);
                if (nonNull(file)) {
                    fileIds.add(file.getId());
                }
            }

            Citation citation = citationCreationService.createCitationWithSourceAndFiles(CreateCitationWithSourceAndFilesParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .citationRequest(citationJsonRequest)
                    .sourceId(source.getId())
                    .filesIds(fileIds)
                    .build());
            params.getCitationMap().put(citationJsonRequest.getId(), citation);
        }
    }
}
