package com.ada.genealogyapp.tree.service;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.export.dto.GedcomRequest;
import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import com.ada.genealogyapp.export.gedcom.GedcomMapperFactory;
import com.ada.genealogyapp.export.json.JsonMapper;
import com.ada.genealogyapp.export.json.JsonMapperFactory;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.dto.TreeExportJsonResponse;
import com.ada.genealogyapp.tree.dto.TreeRequest;
import com.ada.genealogyapp.tree.model.Tree;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TreeExportService {

    private final ObjectMapper objectMapper;

    private final JsonMapperFactory jsonMapperFactory;

    private final GedcomMapperFactory gedcomMapperFactory;

    private final TreeService treeService;

    public String exportTreeToJson(String treeId) throws JsonProcessingException {
        objectMapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);

        Tree tree = treeService.findTreeById(treeId);
        TreeRequest treeRequest = TreeRequest.builder()
                .id(tree.getId())
                .name(tree.getName())
                .build();

        TreeExportJsonResponse response = TreeExportJsonResponse.builder()
                .tree(treeRequest)
                .persons(mapEntitiesJson(treeService.findPersonsById(treeId), Person.class))
                .families(mapEntitiesJson(treeService.findFamiliesById(treeId), Family.class))
                .events(mapEntitiesJson(treeService.findEventsById(treeId), Event.class))
                .citations(mapEntitiesJson(treeService.findCitationsById(treeId), Citation.class))
                .sources(mapEntitiesJson(treeService.findSourcesById(treeId), Source.class))
                .files(mapEntitiesJson(treeService.findFilesById(treeId), File.class))
                .build();

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
    }

    private <E, D> List<D> mapEntitiesJson(Collection<E> entities, Class<E> entityType) {
        JsonMapper<E, D> mapper = jsonMapperFactory.getMapper(entityType);
        return mapper.map(entities);
    }

    public String exportTreeToGedcom(String treeId) {

        Tree tree = treeService.findTreeById(treeId);
        String header = String.join("\n",
                "0 HEAD",
                "1 SOUR " + tree.getName());

        GedcomRequest personsGedcom = mapEntitiesGedcom(treeService.findPersonsById(treeId), null, Person.class);
        GedcomRequest familiesGedcom = mapEntitiesGedcom(treeService.findFamiliesById(treeId), personsGedcom.getGedcomIds(), Family.class);
        GedcomRequest eventsGedcom = mapEntitiesGedcom(treeService.findEventsById(treeId), null, Event.class);
        GedcomRequest citationsGedcom = mapEntitiesGedcom(treeService.findCitationsById(treeId), null, Citation.class);
        GedcomRequest sourcesGedcom = mapEntitiesGedcom(treeService.findSourcesById(treeId), null, Source.class);
        GedcomRequest filesGedcom = mapEntitiesGedcom(treeService.findFilesById(treeId), null, File.class);

        return String.join("\n",
                header,
                personsGedcom.getGedcomString(),
                familiesGedcom.getGedcomString(),
                eventsGedcom.getGedcomString(),
                citationsGedcom.getGedcomString(),
                sourcesGedcom.getGedcomString(),
                filesGedcom.getGedcomString(),
                "0 TRLR"
        );
    }

    private <E> GedcomRequest mapEntitiesGedcom(Set<E> entities, Map<String, String> gedcomIds, Class<E> entityType) {
        GedcomMapper<E> mapper = gedcomMapperFactory.getMapper(entityType);
        return mapper.map(entities, gedcomIds);
    }
}
