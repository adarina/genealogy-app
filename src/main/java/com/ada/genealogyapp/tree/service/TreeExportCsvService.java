package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.dto.CitationExportResponse;
import com.ada.genealogyapp.citation.service.CitationViewService;
import com.ada.genealogyapp.event.dto.EventCitationExportResponse;
import com.ada.genealogyapp.event.dto.EventExportResponse;
import com.ada.genealogyapp.event.dto.EventParticipantExportResponse;
import com.ada.genealogyapp.event.service.EventViewService;
import com.ada.genealogyapp.family.dto.FamilyExportResponse;
import com.ada.genealogyapp.family.service.FamilyViewService;
import com.ada.genealogyapp.file.dto.FileExportResponse;
import com.ada.genealogyapp.file.service.FileViewService;
import com.ada.genealogyapp.location.dto.LocationExportResponse;
import com.ada.genealogyapp.location.service.LocationViewService;
import com.ada.genealogyapp.person.dto.PersonExportResponse;
import com.ada.genealogyapp.person.dto.PersonRelationshipExportResponse;
import com.ada.genealogyapp.person.service.PersonViewService;
import com.ada.genealogyapp.source.dto.SourceExportResponse;
import com.ada.genealogyapp.source.service.SourceViewService;
import com.ada.genealogyapp.tree.dto.TreeResponse;
import com.ada.genealogyapp.tree.dto.params.BaseParams;
import com.opencsv.CSVWriter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;


@Service
@Getter
@EqualsAndHashCode(callSuper = true)
public class TreeExportCsvService extends TreeExportService {


    public TreeExportCsvService(PersonViewService personViewService, FamilyViewService familyViewService,
                                EventViewService eventViewService, CitationViewService citationViewService,
                                SourceViewService sourceViewService, FileViewService fileViewService,
                                LocationViewService locationViewService, TreeViewService treeViewService) {
        super(personViewService, familyViewService, eventViewService, citationViewService, sourceViewService,
                fileViewService, locationViewService, treeViewService);
    }

    @Override
    protected Object assembleOutput(TreeResponse tree, Set<PersonExportResponse> persons, Set<FamilyExportResponse> families, Set<EventExportResponse> events, Set<CitationExportResponse> citations, Set<SourceExportResponse> sources, Set<FileExportResponse> files, Set<LocationExportResponse> locations, BaseParams params) {

        return (StreamingResponseBody) outputStream -> {
            try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
                 CSVWriter csvWriter = new CSVWriter(writer)) {

                String[] header = {
                        "type", "originalId", "secondOriginalId", "name", "firstname", "lastname", "gender", "status",
                        "eventType", "date", "place", "description", "page", "filename", "fileType", "path", "isMain",
                        "locationType", "latitude", "longitude", "eventRelationship", "parentRelationship"
                };

                csvWriter.writeNext(header);

                csvWriter.writeNext(new String[]{
                        "TREE", // type
                        tree.getId(), // originalId
                        "", // secondOriginalId
                        tree.getName(), // name
                        "", // firstname
                        "", // lastname
                        "", // gender
                        "", // status
                        "", // eventType
                        "", // date
                        "", // place
                        "", // description
                        "", // page
                        "", // filename
                        "", // fileType
                        "", // path
                        "", // isMain
                        "", // locationType
                        "", // latitude
                        "", // longitude
                        "", // eventRelationship
                        "" // parentRelationship
                });

                for (PersonExportResponse person : persons) {
                    csvWriter.writeNext(new String[]{
                            "PERSON", // type
                            person.getId(), // originalId
                            "", // secondOriginalId
                            person.getName(), // name
                            person.getFirstname(), // firstname
                            person.getLastname(), // lastname
                            person.getGender() != null ? person.getGender().toString() : "", // gender
                            "", // status
                            "", // eventType
                            "", // date
                            "", // place
                            "", // description
                            "", // page
                            "", // filename
                            "", // fileType
                            "", // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (FamilyExportResponse family : families) {
                    csvWriter.writeNext(new String[]{
                            "FAMILY", // type
                            family.getId(), // originalId
                            "", // secondOriginalId
                            family.getName(), // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            family.getStatus() != null ? family.getStatus().toString() : "", // status
                            "", // eventType
                            "", // date
                            "", // place
                            "", // description
                            "", // page
                            "", // filename
                            "", // fileType
                            "", // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (EventExportResponse event : events) {
                    csvWriter.writeNext(new String[]{
                            "EVENT", // type
                            event.getId(), // originalId
                            "", // secondOriginalId
                            "", // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            "", // status
                            event.getType() != null ? event.getType().toString() : "", // eventType
                            event.getDate(), // date
                            event.getPlace(), // place
                            event.getDescription(), // description
                            "", // page
                            "", // filename
                            "", // fileType
                            "", // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (CitationExportResponse citation : citations) {
                    csvWriter.writeNext(new String[]{
                            "CITATION", // type
                            citation.getId(), // originalId
                            "", // secondOriginalId
                            "", // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            "", // status
                            "", // eventType
                            citation.getDate(), // date
                            "", // place
                            "", // description
                            citation.getPage(), // page
                            "", // filename
                            "", // fileType
                            "", // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (SourceExportResponse source : sources) {
                    csvWriter.writeNext(new String[]{
                            "SOURCE", // type
                            source.getId(), // originalId
                            "", // secondOriginalId
                            source.getName(), // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            "", // status
                            "", // eventType
                            "", // date
                            "", // place
                            "", // description
                            "", // page
                            "", // filename
                            "", // fileType
                            "", // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (FileExportResponse file : files) {
                    csvWriter.writeNext(new String[]{
                            "FILE", // type
                            file.getId(), // originalId
                            "", // secondOriginalId
                            file.getName(), // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            "", // status
                            "", // eventType
                            "", // date
                            "", // place
                            "", // description
                            "", // page
                            file.getFilename(), // filename
                            file.getType(), // fileType
                            file.getPath(), // path
                            "", // isMain
                            "", // locationType
                            "", // latitude
                            "", // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (LocationExportResponse location : locations) {
                    csvWriter.writeNext(new String[]{
                            "LOCATION", // type
                            location.getId(), // originalId
                            "", // secondOriginalId
                            location.getName(), // name
                            "", // firstname
                            "", // lastname
                            "", // gender
                            "", // status
                            "", // eventType
                            "", // date
                            "", // place
                            "", // description
                            "", // page
                            "", // filename
                            "", // fileType
                            "", // path
                            String.valueOf(location.getIsMain()), // isMain
                            location.getType() != null ? location.getType().toString() : "", // locationType
                            String.valueOf(location.getLatitude()), // latitude
                            String.valueOf(location.getLongitude()), // longitude
                            "", // eventRelationship
                            "" // parentRelationship
                    });
                }

                for (PersonExportResponse person : persons) {
                    if (Objects.nonNull(person.getRelationships())) {
                        for (PersonRelationshipExportResponse relationship : person.getRelationships()) {
                            csvWriter.writeNext(new String[]{
                                    "PARENT_OF", // type
                                    person.getId(), // originalId
                                    relationship.getChildId(), // secondOriginalId
                                    "", // name
                                    "", // firstname
                                    "", // lastname
                                    "", // gender
                                    "", // status
                                    "", // eventType
                                    "", // date
                                    "", // place
                                    "", // description
                                    "", // page
                                    "", // filename
                                    "", // fileType
                                    "", // path
                                    "", // isMain
                                    "", // locationType
                                    "", // latitude
                                    "", // longitude
                                    "", // eventRelationship
                                    relationship.getRelationship() // parentRelationship
                            });
                        }
                    }
                }

                for (FamilyExportResponse family : families) {
                    if (Objects.nonNull(family.getFatherId())) {
                        csvWriter.writeNext(new String[]{
                                "HAS_FATHER", // type
                                family.getId(), // originalId
                                family.getFatherId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                    if (Objects.nonNull(family.getMotherId())) {
                        csvWriter.writeNext(new String[]{
                                "HAS_MOTHER", // type
                                family.getId(), // originalId
                                family.getMotherId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                    for (String childId : family.getChildrenIds()) {
                        csvWriter.writeNext(new String[]{
                                "HAS_CHILD", // type
                                family.getId(), // originalId
                                childId, // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                }

                for (EventExportResponse event : events) {
                    for (EventParticipantExportResponse participantExportResponse : event.getParticipants()) {
                        csvWriter.writeNext(new String[]{
                                "HAS_PARTICIPANT", // type
                                event.getId(), // originalId
                                participantExportResponse.getParticipantId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                participantExportResponse.getRelationship(), // eventRelationship
                                "" // parentRelationship
                        });
                    }

                    for (EventCitationExportResponse citationExportResponse : event.getCitations()) {
                        csvWriter.writeNext(new String[]{
                                "HAS_EVENT_CITATION", // type
                                event.getId(), // originalId
                                citationExportResponse.getCitationId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }

                    if (Objects.nonNull(event.getLocationId())) {
                        csvWriter.writeNext(new String[]{
                                "HAS_EVENT_LOCATION", // type
                                event.getId(), // originalId
                                event.getLocationId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                }

                for (LocationExportResponse location : locations) {
                    if (Objects.nonNull(location.getLocationId()))
                        csvWriter.writeNext(new String[]{
                                "LOCATED_IN", // type
                                location.getId(), // originalId
                                location.getLocationId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                }

                for (CitationExportResponse citation : citations) {
                    if (Objects.nonNull(citation.getSourceId())) {
                        csvWriter.writeNext(new String[]{
                                "HAS_CITATION_SOURCE", // type
                                citation.getId(), // originalId
                                citation.getSourceId(), // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                    for (String fileId : citation.getFilesIds()) {
                        csvWriter.writeNext(new String[]{
                                "HAS_CITATION_FILE", // type
                                citation.getId(), // originalId
                                fileId, // secondOriginalId
                                "", // name
                                "", // firstname
                                "", // lastname
                                "", // gender
                                "", // status
                                "", // eventType
                                "", // date
                                "", // place
                                "", // description
                                "", // page
                                "", // filename
                                "", // fileType
                                "", // path
                                "", // isMain
                                "", // locationType
                                "", // latitude
                                "", // longitude
                                "", // eventRelationship
                                "" // parentRelationship
                        });
                    }
                }
            } catch (IOException e) {  
                throw new RuntimeException("Error writing CSV stream", e);
            }
        };
    }
}
