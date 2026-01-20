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

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static com.ada.genealogyapp.gedcom.mappers.EventMapper.getGedcomEventTag;
import static com.ada.genealogyapp.gedcom.mappers.GenderMapper.getGedcomGenderTag;
import static java.util.Objects.nonNull;

@Service
@Getter
@EqualsAndHashCode(callSuper = true)
public class TreeExportGedcomService extends TreeExportService {

    // TODO LONG LAT
    // TODO TOO SLOW
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

    @Override
    protected Object assembleOutput(TreeResponse tree, Set<PersonExportResponse> persons, Set<FamilyExportResponse> families,
                                    Set<EventExportResponse> events, Set<CitationExportResponse> citations,
                                    Set<SourceExportResponse> sources, Set<FileExportResponse> files,
                                    Set<LocationExportResponse> locations, BaseParams params) {

        try (StringWriter stringWriter = new StringWriter()) {
            exportGedcomStream(stringWriter, params, persons, families, sources, files);
            return stringWriter.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error writing GEDCOM stream", e);
        }
    }

    public void exportGedcomStream(Writer writer, BaseParams params,
                                   Set<PersonExportResponse> persons, Set<FamilyExportResponse> families,
                                   Set<SourceExportResponse> sources, Set<FileExportResponse> files) throws IOException {

        BufferedWriter gedcomWriter = new BufferedWriter(writer);

        generateHeader(gedcomWriter);
        generateFiles(files, gedcomWriter);
        generateSources(sources, gedcomWriter);
        generatePersons(persons, params, gedcomWriter);
        generateFamilies(families, params, gedcomWriter);
        generateFooter(gedcomWriter);
        gedcomWriter.flush();
    }


    private String generateGedcomId(String uuid) {
        return uuid.replaceAll("-", "");
    }

    private void generateCitationsAndFiles(BaseParams params, BufferedWriter writer, EventCitationResponse citation) throws IOException {
        GetCitationParams getCitationParams = GetCitationParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .citationId(citation.getId())
                .build();

        CitationSourceResponse citationSource = citationViewService.getCitation(getCitationParams);
        if (nonNull(citationSource)) {
            if (nonNull(citationSource.getSourceId())) {
                writer.write("2 " + SourceGedcomType.SOUR + " @" + generateGedcomId(citationSource.getSourceId()) + "@");
                writer.newLine();
            }
            if (nonNull(citation.getPage())) {
                writer.write("3 " + SourceGedcomType.PAGE + " " + citation.getPage());
                writer.newLine();
            }
            if (nonNull(citation.getDate())) {
                writer.write("4 " + EventGedcomType.DATE + " " + citation.getDate());
                writer.newLine();
            }
        }

        List<FileResponse> fileResponses = citationFilesViewService.getCitationFiles(getCitationParams);
        for (FileResponse file : fileResponses) {
            if (nonNull(citationSource.getSourceId())) {
                writer.write("2 " + FileGedcomType.OBJE + " @" + generateGedcomId(file.getId()) + "@");
                writer.newLine();
            }
        }
    }

    private void generateEvents(BaseParams params, BufferedWriter writer, String participantId) throws IOException {
        List<ParticipantEventGedcomResponse> participantEvents = participantEventsViewService.getParticipantEventsGedcom(BaseParticipantParams.builder()
                .userId(params.getUserId())
                .treeId(params.getTreeId())
                .participantId(participantId)
                .build());

        for (ParticipantEventGedcomResponse event : participantEvents) {
            writer.write("1 " + getGedcomEventTag(event.getType()));
            writer.newLine();
            if (nonNull(event.getDescription())) {
                writer.write("2 " + EventGedcomType.TYPE + " " + event.getDescription());
                writer.newLine();
            }
            if (nonNull(event.getDate())) {
                writer.write("2 " + EventGedcomType.DATE + " " + event.getDate());
                writer.newLine();
            }
            if (nonNull(event.getPlace())) {
                writer.write("2 " + LocationGedcomType.PLAC + " " + event.getPlace());
                writer.newLine();
            }
            for (EventCitationResponse citation : event.getCitations()) {
                generateCitationsAndFiles(params, writer, citation);
            }
        }
    }

    private void generatePersons(Set<PersonExportResponse> persons, BaseParams params, BufferedWriter writer) throws IOException {
        for (PersonExportResponse person : persons) {
            String personId = person.getId();
            String gedcomPersonId = generateGedcomId(personId);

            writer.write("0 @" + gedcomPersonId + "@ " + PersonGedcomType.INDI);
            writer.newLine();
            writer.write("1 " + PersonGedcomType.NAME + " " + person.getFirstname() + " /" + person.getLastname() + "/");
            writer.newLine();
            writer.write("2 " + PersonGedcomType.GIVN + " " + person.getFirstname());
            writer.newLine();
            writer.write("2 " + PersonGedcomType.SURN + " " + person.getLastname());
            writer.newLine();
            writer.write("1 " + PersonGedcomType.SEX + " " + getGedcomGenderTag(person.getGender()));
            writer.newLine();

            generateEvents(params, writer, personId);

            List<PersonFamilyGedcomResponse> families = personFamiliesViewService.getPersonFamiliesGedcom(GetPersonParams.builder()
                    .userId(params.getUserId())
                    .treeId(params.getTreeId())
                    .personId(personId)
                    .build());

            for (PersonFamilyGedcomResponse family : families) {
                if (nonNull(family) && !family.getIsParent()) {
                    writer.write("1 " + FamilyGedcomType.FAMC + " @" + generateGedcomId(family.getId()) + "@");
                    writer.newLine();
                    writer.write("2 " + FamilyGedcomType.PEDI + " birth");
                    writer.newLine();
                }
            }
            for (PersonFamilyGedcomResponse family : families) {
                if (nonNull(family) && family.getIsParent()) {
                    writer.write("1 " + FamilyGedcomType.FAMS + " @" + generateGedcomId(family.getId()) + "@");
                    writer.newLine();
                }
            }
        }
    }

    private void generateFamilies(Set<FamilyExportResponse> families, BaseParams params, BufferedWriter writer) throws IOException {
        for (FamilyExportResponse family : families) {
            String familyId = family.getId();
            String gedcomFamilyId = generateGedcomId(familyId);

            writer.write("0 @" + gedcomFamilyId + "@ " + FamilyGedcomType.FAM);
            writer.newLine();
            if (nonNull(family.getFatherId())) {
                writer.write("1 " + FamilyGedcomType.HUSB + " @" + generateGedcomId(family.getFatherId()) + "@");
                writer.newLine();
            }
            if (nonNull(family.getMotherId())) {
                writer.write("1 " + FamilyGedcomType.WIFE + " @" + generateGedcomId(family.getMotherId()) + "@");
                writer.newLine();
            }
            generateEvents(params, writer, familyId);
            if (nonNull(family.getChildrenIds())) {
                for (String childId : family.getChildrenIds()) {
                    if (nonNull(childId)) {
                        writer.write("1 " + FamilyGedcomType.CHIL + " @" + generateGedcomId(childId) + "@");
                        writer.newLine();
                    }
                }
            }
        }
    }

    private void generateSources(Set<SourceExportResponse> sources, BufferedWriter writer) throws IOException {
        for (SourceExportResponse source : sources) {
            String sourceId = source.getId();
            String gedcomSourceId = generateGedcomId(sourceId);

            writer.write("0 @" + gedcomSourceId + "@ " + SourceGedcomType.SOUR);
            writer.newLine();
            if (nonNull(source.getName())) {
                writer.write("1 " + SourceGedcomType.TITL + " " + source.getName());
                writer.newLine();
            }
            writer.write("1 " + SourceGedcomType.PUBL + " ");
            writer.newLine();
        }
    }

    private void generateFiles(Set<FileExportResponse> files, BufferedWriter writer) throws IOException {
        for (FileExportResponse file : files) {
            String fileId = file.getId();
            String gedcomFileId = generateGedcomId(fileId);

            writer.write("0 @" + gedcomFileId + "@ " + FileGedcomType.OBJE);
            writer.newLine();
            if (nonNull(file.getPath())) {
                writer.write("1 " + FileGedcomType.FILE + " " + file.getName());
                writer.newLine();
            }
            if (nonNull(file.getType())) {
                writer.write("1 " + FileGedcomType.FORM + " " + file.getType());
                writer.newLine();
            }
            if (nonNull(file.getName())) {
                writer.write("1 " + FileGedcomType.TITL + " " + file.getName());
                writer.newLine();
            }
        }
    }

    //TODO time for tests
    private void generateHeader(BufferedWriter writer) throws IOException {
        writer.write("0 HEAD");
        writer.newLine();
        writer.write("1 SOUR Genealogy App");
        writer.newLine();
        writer.write("2 VERS 1.0");
        writer.newLine();
        writer.write("1 NAME Genealogy App");
        writer.newLine();
        writer.write("1 DATE " + LocalDate.now().format(DateTimeFormatter.ofPattern("d MMM yyyy")).toUpperCase());
        writer.newLine();
        writer.write("2 TIME " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")).toUpperCase());
        writer.newLine();
        writer.write("1 SUBM @SUBM@");
        writer.newLine();
        writer.write("1 GEDC");
        writer.newLine();
        writer.write("2 VERS 5.5.1");
        writer.newLine();
        writer.write("2 FORM LINEAGE-LINKED");
        writer.newLine();
        writer.write("1 CHAR UTF-8");
        writer.newLine();
        writer.write("0 @SUBM@ SUBM");
        writer.newLine();
        writer.write("1 NAME");
        writer.newLine();
    }

    private void generateFooter(BufferedWriter writer) throws IOException {
        writer.write("0 TRLR");
        writer.newLine();
    }
}
