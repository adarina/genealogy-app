package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationExportResponse;
import com.ada.genealogyapp.citation.dto.CitationSourceResponse;
import com.ada.genealogyapp.citation.dto.params.GetCitationParams;
import com.ada.genealogyapp.citation.service.CitationFilesViewService;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.ada.genealogyapp.event.dto.EventCitationResponse;
import com.ada.genealogyapp.event.dto.EventExportResponse;
import com.ada.genealogyapp.event.service.EventViewService;
import com.ada.genealogyapp.family.dto.FamilyExportResponse;
import com.ada.genealogyapp.family.service.FamilyViewService;
import com.ada.genealogyapp.file.dto.FileExportResponse;
import com.ada.genealogyapp.file.dto.FileResponse;
import com.ada.genealogyapp.file.service.FileViewService;
import com.ada.genealogyapp.gedcom.type.*;
import com.ada.genealogyapp.location.dto.LocationExportResponse;
import com.ada.genealogyapp.location.service.LocationViewService;
import com.ada.genealogyapp.participant.dto.BaseParticipantParams;
import com.ada.genealogyapp.participant.dto.ParticipantEventGedcomResponse;
import com.ada.genealogyapp.participant.service.ParticipantEventsViewService;
import com.ada.genealogyapp.person.dto.PersonFamilyGedcomResponse;
import com.ada.genealogyapp.person.dto.PersonExportResponse;
import com.ada.genealogyapp.person.dto.params.GetPersonParams;
import com.ada.genealogyapp.person.service.PersonFamiliesViewService;
import com.ada.genealogyapp.person.service.PersonViewService;
import com.ada.genealogyapp.source.dto.SourceExportResponse;
import com.ada.genealogyapp.source.service.SourceViewService;
import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.ada.genealogyapp.gedcom.mappers.EventMapper.getGedcomEventTag;
import static com.ada.genealogyapp.gedcom.mappers.GenderMapper.getGedcomGenderTag;
import static java.util.Objects.nonNull;

@Service
@Getter
@EqualsAndHashCode(callSuper = true)
public class TreeExportGedcomService extends TreeExportService {

    private final ParticipantEventsViewService participantEventsViewService;

    private final CitationFilesViewService citationFilesViewService;

    private final PersonFamiliesViewService personFamiliesViewService;

    public TreeExportGedcomService(PersonViewService personViewService, FamilyViewService familyViewService, EventViewService eventViewService,
                                   CitationViewService citationViewService, SourceViewService sourceViewService, FileViewService fileViewService,
                                   LocationViewService locationViewService, TreeViewService treeViewService, ParticipantEventsViewService participantEventsViewService,
                                   CitationFilesViewService citationFilesViewService, PersonFamiliesViewService personFamiliesViewService) {
        super(personViewService, familyViewService, eventViewService, citationViewService, sourceViewService,
                fileViewService, locationViewService, treeViewService);
        this.participantEventsViewService = participantEventsViewService;
        this.citationFilesViewService = citationFilesViewService;
        this.personFamiliesViewService = personFamiliesViewService;
    }


    //TODO LONG LAT
    @Override
    protected Object assembleOutput(TreeResponse tree, Set<PersonExportResponse> persons, Set<FamilyExportResponse> families,
                                    Set<EventExportResponse> events, Set<CitationExportResponse> citations,
                                    Set<SourceExportResponse> sources, Set<FileExportResponse> files,
                                    Set<LocationExportResponse> locations, BaseParams params) {

        List<String> gedcomOutput = new ArrayList<>();

        generateHeader(gedcomOutput);
        generateFiles(files, gedcomOutput);
        generateSources(sources, gedcomOutput);
        generatePersons(persons, params, gedcomOutput);
        generateFamilies(families, params, gedcomOutput);
        generateFooter(gedcomOutput);

        return String.join("\n", gedcomOutput);
    }

    private String generateGedcomId(String uuid) {
        return uuid.replaceAll("-", "");
    }

    private void generateCitationsAndFiles(BaseParams params, List<String> gedcomOutput, EventCitationResponse citation) {
        GetCitationParams getCitationParams = GetCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(citation.getId())
                .build();

        CitationSourceResponse citationSource = citationViewService.getCitation(getCitationParams);
        if (nonNull(citationSource)) {
            if (nonNull(citationSource.getSourceId())) {
                gedcomOutput.add("2 " + SourceGedcomType.SOUR + " @" + generateGedcomId(citationSource.getSourceId()) + "@");
            }
            if (nonNull(citation.getPage())) {
                gedcomOutput.add("3 " + SourceGedcomType.PAGE + " " + citation.getPage());
            }
            if (nonNull(citation.getDate())) {
                gedcomOutput.add("4 " + EventGedcomType.DATE + " " + citation.getDate());
            }
        }

        List<FileResponse> fileResponses = citationFilesViewService.getCitationFiles(getCitationParams);
        for (FileResponse file : fileResponses) {
            if (nonNull(citationSource.getSourceId())) {
                gedcomOutput.add("2 " + FileGedcomType.OBJE + " @" + generateGedcomId(file.getId()) + "@");
            }
        }
    }

    private void generateEvents(BaseParams params, List<String> gedcomOutput, String participantId) {
        List<ParticipantEventGedcomResponse> participantEvents = participantEventsViewService.getParticipantEventsGedcom(BaseParticipantParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .participantId(participantId)
                .build());

        for (ParticipantEventGedcomResponse event : participantEvents) {
            gedcomOutput.add("1 " + getGedcomEventTag(event.getType()));
            if (nonNull(event.getDescription())) {
                gedcomOutput.add("2 " + EventGedcomType.TYPE + " " + event.getDescription());
            }
            if (nonNull(event.getDate())) {
                gedcomOutput.add("2 " + EventGedcomType.DATE + " " + event.getDate());
            }
            if (nonNull(event.getPlace())) {
                gedcomOutput.add("2 " + LocationGedcomType.PLAC + " " + event.getPlace());
            }
            for (EventCitationResponse citation : event.getCitations()) {
                generateCitationsAndFiles(params, gedcomOutput, citation);
            }
        }
    }

    private void generatePersons(Set<PersonExportResponse> persons, BaseParams params, List<String> gedcomOutput) {
        for (PersonExportResponse person : persons) {
            String personId = person.getId();
            String gedcomPersonId = generateGedcomId(personId);

            gedcomOutput.add("0 @" + gedcomPersonId + "@ " + PersonGedcomType.INDI);
            gedcomOutput.add("1 " + PersonGedcomType.NAME + " " + person.getFirstname() + " /" + person.getLastname() + "/");
            gedcomOutput.add("2 " + PersonGedcomType.GIVN + " " + person.getFirstname());
            gedcomOutput.add("2 " + PersonGedcomType.SURN + " " + person.getLastname());
            gedcomOutput.add("1 " + PersonGedcomType.SEX + " " + getGedcomGenderTag(person.getGender()));

            generateEvents(params, gedcomOutput, personId);

            List<PersonFamilyGedcomResponse> families = personFamiliesViewService.getPersonFamiliesGedcom(GetPersonParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTreeId())
                    .personId(personId)
                    .build());

            for (PersonFamilyGedcomResponse family : families) {
                if (nonNull(family) && !family.getIsParent()) {
                    gedcomOutput.add("1 " + FamilyGedcomType.FAMC + " @" + generateGedcomId(family.getId()) + "@");
                    gedcomOutput.add("2 " + FamilyGedcomType.PEDI + " birth");
                }
            }
            for (PersonFamilyGedcomResponse family : families) {
                if (nonNull(family) && family.getIsParent()) {
                    gedcomOutput.add("1 " + FamilyGedcomType.FAMS + " @" + generateGedcomId(family.getId()) + "@");
                }
            }
        }
    }

    private void generateFamilies(Set<FamilyExportResponse> families, BaseParams params, List<String> gedcomOutput) {
        for (FamilyExportResponse family : families) {
            String familyId = family.getId();
            String gedcomFamilyId = generateGedcomId(familyId);

            gedcomOutput.add("0 @" + gedcomFamilyId + "@ " + FamilyGedcomType.FAM);
            if (nonNull(family.getFatherId())) {
                gedcomOutput.add("1 " + FamilyGedcomType.HUSB + " @" + generateGedcomId(family.getFatherId()) + "@");
            }
            if (nonNull(family.getMotherId())) {
                gedcomOutput.add("1 " + FamilyGedcomType.WIFE + " @" + generateGedcomId(family.getMotherId()) + "@");
            }
            generateEvents(params, gedcomOutput, familyId);
            if (nonNull(family.getChildrenIds())) {
                for (String childId : family.getChildrenIds()) {
                    if (nonNull(childId)) {
                        gedcomOutput.add("1 " + FamilyGedcomType.CHIL + " @" + generateGedcomId(childId) + "@");
                    }
                }
            }
        }
    }

    private void generateSources(Set<SourceExportResponse> sources, List<String> gedcomOutput) {
        for (SourceExportResponse source : sources) {
            String sourceId = source.getId();
            String gedcomSourceId = generateGedcomId(sourceId);

            gedcomOutput.add("0 @" + gedcomSourceId + "@ " + SourceGedcomType.SOUR);
            if (nonNull(source.getName())) {
                gedcomOutput.add("1 " + SourceGedcomType.TITL + " " + source.getName());
            }
            gedcomOutput.add("1 " + SourceGedcomType.PUBL + " ");
        }
    }

    private void generateFiles(Set<FileExportResponse> files, List<String> gedcomOutput) {
        for (FileExportResponse file : files) {
            String fileId = file.getId();
            String gedcomFileId = generateGedcomId(fileId);

            gedcomOutput.add("0 @" + gedcomFileId + "@ " + FileGedcomType.OBJE);
            if (nonNull(file.getPath())) {
                gedcomOutput.add("1 " + FileGedcomType.FILE + " " + file.getName());
            }
            if (nonNull(file.getType())) {
                gedcomOutput.add("1 " + FileGedcomType.FORM + " " + file.getType());
            }
            if (nonNull(file.getName())) {
                gedcomOutput.add("1 " + FileGedcomType.TITL + " " + file.getName());
            }
        }
    }

    //TODO time do testów
    private void generateHeader(List<String> gedcomOutput) {
        gedcomOutput.add("0 HEAD");
        gedcomOutput.add("1 SOUR Genealogy App");
        gedcomOutput.add("2 VERS 1.0");
        gedcomOutput.add("1 NAME Genealogy App");
        gedcomOutput.add("1 DATE " + LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy")).toUpperCase());
        gedcomOutput.add("2 TIME " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toUpperCase());
        gedcomOutput.add("1 SUBM @SUBM@");
        gedcomOutput.add("1 GEDC");
        gedcomOutput.add("2 VERS 5.5.1");
        gedcomOutput.add("2 FORM LINEAGE-LINKED");
        gedcomOutput.add("1 CHAR UTF-8");
        gedcomOutput.add("0 @SUBM@ SUBM");
        gedcomOutput.add("1 NAME");
    }

    private void generateFooter(List<String> gedcomOutput) {
        gedcomOutput.add("0 TRLR");
    }
}
