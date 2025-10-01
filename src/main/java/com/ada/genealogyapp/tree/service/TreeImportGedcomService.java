package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationRequest;
import com.ada.genealogyapp.citation.dto.params.AddFileToCitationParams;
import com.ada.genealogyapp.citation.dto.params.CreateCitationWithSourceAndEventParams;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.citation.service.CitationCreationService;
import com.ada.genealogyapp.citation.service.CitationService;
import com.ada.genealogyapp.event.dto.params.AddLocationToEventParams;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.service.EventCreationService;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.event.type.EventParticipantRelationshipType;
import com.ada.genealogyapp.event.type.EventType;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.dto.FamilyRequest;
import com.ada.genealogyapp.family.dto.params.AddChildToFamilyRequestParams;
import com.ada.genealogyapp.family.dto.params.AddPersonToFamilyParams;
import com.ada.genealogyapp.family.dto.params.CreateFamilyRequestParams;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.family.service.FamilyCreationService;
import com.ada.genealogyapp.family.service.FamilyService;
import com.ada.genealogyapp.family.type.StatusType;
import com.ada.genealogyapp.file.dto.FileRequest;
import com.ada.genealogyapp.file.dto.params.CreateFileRequestParams;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.file.service.FileCreationService;
import com.ada.genealogyapp.gedcom.utils.GedcomUtils;
import com.ada.genealogyapp.gedcom.dto.*;
import com.ada.genealogyapp.graphuser.service.GraphUserViewService;
import com.ada.genealogyapp.tree.dto.params.TreeImportGedcomParams;
import com.ada.genealogyapp.gedcom.type.EventGedcomType;
import com.ada.genealogyapp.gedcom.type.LocationGedcomType;
import com.ada.genealogyapp.location.dto.params.AddressParams;
import com.ada.genealogyapp.location.dto.params.CreateLocationAndHierarchyParams;
import com.ada.genealogyapp.location.model.Location;
import com.ada.genealogyapp.location.service.LocationCreationService;
import com.ada.genealogyapp.participant.dto.ParticipantEventRequest;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.dto.PersonRequest;
import com.ada.genealogyapp.person.dto.params.CreateEventRequestWithParticipantParams;
import com.ada.genealogyapp.person.dto.params.CreatePersonRequestParams;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.service.PersonCreationService;
import com.ada.genealogyapp.person.type.GenderType;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import com.ada.genealogyapp.source.dto.SourceRequest;
import com.ada.genealogyapp.source.dto.params.CreateSourceRequestParams;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.source.service.SourceCreationService;
import com.ada.genealogyapp.tree.dto.TreeImportGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.FamilyGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.FileGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.PersonGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.SourceGedcomRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static com.ada.genealogyapp.gedcom.mappers.EventMapper.mapEvent;
import static com.ada.genealogyapp.gedcom.mappers.GenderMapper.extractGender;
import static com.ada.genealogyapp.gedcom.utils.GedcomUtils.parseCoordinateFromExtensions;
import static java.util.Objects.nonNull;

@Service
@EqualsAndHashCode(callSuper = true)
public class TreeImportGedcomService extends TreeImportService<TreeImportGedcomRequest, TreeImportGedcomParams> {

    private final ObjectMapper objectMapper;
    private final SourceCreationService sourceCreationService;
    private final FileCreationService fileCreationService;
    private final PersonCreationService personCreationService;
    private final LocationCreationService locationCreationService;
    private final EventCreationService eventCreationService;
    private final EventService eventService;
    private final CitationCreationService citationCreationService;
    private final CitationService citationService;
    private final FamilyCreationService familyCreationService;
    private final FamilyService familyService;

    public TreeImportGedcomService(TreeCreationService treeCreationService, GraphUserViewService graphUserViewService,
                                   ObjectMapper objectMapper, SourceCreationService sourceCreationService,
                                   FileCreationService fileCreationService, PersonCreationService personCreationService,
                                   LocationCreationService locationCreationService, EventCreationService eventCreationService,
                                   EventService eventService, CitationCreationService citationCreationService,
                                   CitationService citationService, FamilyCreationService familyCreationService, FamilyService familyService) {
        super(graphUserViewService, treeCreationService);
        this.objectMapper = objectMapper;
        this.sourceCreationService = sourceCreationService;
        this.fileCreationService = fileCreationService;
        this.personCreationService = personCreationService;
        this.locationCreationService = locationCreationService;
        this.eventCreationService = eventCreationService;
        this.eventService = eventService;
        this.citationCreationService = citationCreationService;
        this.citationService = citationService;
        this.familyCreationService = familyCreationService;
        this.familyService = familyService;
    }

    @Override
    protected TreeImportGedcomRequest parseInputFile(InputStream inputStream) throws IOException, SAXParseException {
        return objectMapper.readValue(GedcomUtils.convertGedcomToJson(inputStream), TreeImportGedcomRequest.class);
    }

    @Override
    protected TreeImportGedcomParams initializeImportParams(Tree tree, String userId) {
        return TreeImportGedcomParams.builder()
                .tree(tree)
                .userId(userId)
                .build();
    }

    @Override
    protected void processEntities(TreeImportGedcomRequest importRequest, TreeImportGedcomParams params) {
        List<EntityProcessor<TreeImportGedcomRequest, TreeImportGedcomParams>> processors = Arrays.asList(
                this::processSources,
                this::processFiles,
                this::processPersons,
                this::processFamilies
        );
        processAllEntities(importRequest, params, processors);
    }

    @Override
    public void processSources(TreeImportGedcomRequest request, TreeImportGedcomParams params) {
        for (SourceGedcomRequest sourceRequest : request.getSources()) {
            Source source = sourceCreationService.createSource(CreateSourceRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .sourceRequest(SourceRequest.builder()
                            .name(sourceRequest.getTitl())
                            .build())
                    .build());
            params.getSourceMap().put(sourceRequest.getId(), source);
        }
    }

    @Override
    public void processFiles(TreeImportGedcomRequest request, TreeImportGedcomParams params) {
        for (FileGedcomRequest fileRequest : request.getMedia()) {
            String type = fileRequest.getExtensions().getMoreTags().get(0).getValue();
            String name = fileRequest.getExtensions().getMoreTags().get(1).getValue() + "." + type;
            String path = fileRequest.get_file() + "." + type;

            File file = fileCreationService.createFile(CreateFileRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .fileRequest(FileRequest.builder()
                            .path(path)
                            .type(type)
                            .name(name)
                            .build())
                    .build());
            params.getFileMap().put(fileRequest.getId(), file);
        }
    }

    @Override
    public void processPersons(TreeImportGedcomRequest request, TreeImportGedcomParams params) {
        for (PersonGedcomRequest personRequest : request.getPeople()) {
            GenderType gender = extractGender(personRequest);
            Person person = personCreationService.createPerson(CreatePersonRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .personRequest(PersonRequest.builder()
                            .firstname(personRequest.getNames().get(0).getGivn())
                            .lastname(personRequest.getNames().get(0).getSurn())
                            .gender(gender)
                            .build())
                    .build());
            params.getPersonMap().put(personRequest.getId(), person);
            params.getParticipantMap().put(personRequest.getId(), person);
            processParticipantEvents(personRequest.getEventsFacts(), person, params, EventParticipantRelationshipType.MAIN);
        }
    }

    private void processParticipantEvents(List<EventFact> eventsFacts, Participant participant, TreeImportGedcomParams params, EventParticipantRelationshipType relationship) {
        if (nonNull(eventsFacts)) {
            for (EventFact eventFact : eventsFacts) {
                EventType type = mapEvent(EventGedcomType.valueOf(eventFact.getTag()));
                if (type != EventType.EVENT && type != EventType.ERROR) {
                    Location location = locationCreationService.createLocationAndHierarchy(CreateLocationAndHierarchyParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .hierarchy(eventFact.getPlace())
                            .address(nonNull(eventFact.getAddr()) ? AddressParams.builder()
                                    .country(eventFact.getAddr().getCtry())
                                    .state(eventFact.getAddr().getStae())
                                    .city(eventFact.getAddr().getCity())
                                    .build() : null)
                            .latitude(parseCoordinateFromExtensions(eventFact.getExtensions(), LocationGedcomType.LATI.name()))
                            .longitude(parseCoordinateFromExtensions(eventFact.getExtensions(), LocationGedcomType.LONG.name()))
                            .build());

                    Event event = eventCreationService.createEventWithParticipant(
                            CreateEventRequestWithParticipantParams.builder()
                                    .userId(params.getUserId())
                                    .treeId(params.getTree().getId())
                                    .eventRequest(ParticipantEventRequest.builder()
                                            .place(eventFact.getPlace())
                                            .type(type)
                                            .date(eventFact.getDate())
                                            .description(eventFact.getType())
                                            .relationship(relationship)
                                            .build())
                                    .participantId(participant.getId())
                                    .relationshipType(relationship.name())
                                    .build());

                    if (nonNull(location)) {
                        eventService.addLocationToEvent(AddLocationToEventParams.builder()
                                .userId(params.getUserId())
                                .treeId(params.getTree().getId())
                                .eventId(event.getId())
                                .locationId(location.getId())
                                .build());
                    }
                    processEventCitations(event.getId(), eventFact.getSourceCitations(), params);
                }
            }
        }
    }

    private void processEventCitations(String eventId, List<SourceCitation> sourceCitations, TreeImportGedcomParams params) {
        if (nonNull(sourceCitations)) {
            for (SourceCitation sourceCitation : sourceCitations) {
                Source source = params.getSourceMap().get(sourceCitation.getRef());
                Citation citation = citationCreationService.createCitationWithSourceAndEvent(
                        CreateCitationWithSourceAndEventParams.builder()
                                .userId(params.getUserId())
                                .treeId(params.getTree().getId())
                                .citationRequest(CitationRequest.builder()
                                        .page(sourceCitation.getPage())
                                        .date(sourceCitation.getDate())
                                        .build())
                                .sourceId(source.getId())
                                .eventId(eventId)
                                .build());

                if (nonNull(sourceCitation.getMediaRefs())) {
                    for (MediaRef ref : sourceCitation.getMediaRefs()) {
                        File file = params.getFileMap().get(ref.getRef());
                        if (nonNull(file)) {
                            citationService.addFileToCitation(AddFileToCitationParams.builder()
                                    .userId(params.getUserId())
                                    .treeId(params.getTree().getId())
                                    .citationId(citation.getId())
                                    .fileId(file.getId())
                                    .build());
                        }
                    }
                }
            }
        }
    }

    @Override
    public void processFamilies(TreeImportGedcomRequest request, TreeImportGedcomParams params) {
        for (FamilyGedcomRequest familyRequest : request.getFamilies()) {
            Family family = familyCreationService.createFamily(CreateFamilyRequestParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTree().getId())
                    .familyRequest(FamilyRequest.builder().status(StatusType.MARRIED).build())
                    .build());

            params.getFamilyMap().put(familyRequest.getId(), family);
            params.getParticipantMap().put(familyRequest.getId(), family);

            if (nonNull(familyRequest.getHusbandRefs())) {
                Person father = params.getPersonMap().get(familyRequest.getHusbandRefs().get(0).getRef());
                if (nonNull(father)) {
                    familyService.addFatherToFamily(AddPersonToFamilyParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .familyId(family.getId())
                            .personId(father.getId())
                            .build());
                }
            }
            if (nonNull(familyRequest.getWifeRefs())) {
                Person mother = params.getPersonMap().get(familyRequest.getWifeRefs().get(0).getRef());
                if (nonNull(mother)) {
                    familyService.addMotherToFamily(AddPersonToFamilyParams.builder()
                            .userId(params.getUserId())
                            .treeId(params.getTree().getId())
                            .familyId(family.getId())
                            .personId(mother.getId())
                            .build());
                }
            }

            if (nonNull(familyRequest.getChildRefs())) {
                for (Reference childRef : familyRequest.getChildRefs()) {
                    Person child = params.getPersonMap().get(childRef.getRef());
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
            processParticipantEvents(familyRequest.getEventsFacts(), family, params, EventParticipantRelationshipType.FAMILY);
        }
    }
}
