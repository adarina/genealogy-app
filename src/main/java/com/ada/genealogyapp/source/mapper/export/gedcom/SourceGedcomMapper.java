package com.ada.genealogyapp.source.mapper.export.gedcom;

import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import com.ada.genealogyapp.source.model.Source;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SourceGedcomMapper extends GedcomMapper<Source> {

    @Override
    public Class<Source> getEntityType() {
        return Source.class;
    }


    @Override
    public String generateGedcomContent(Source source, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();

        builder.append("0 ").append(gedcomId).append(" SOUR\n");
        builder.append("1 TITL ").append(source.getName()).append("\n");

        return builder.toString();
    }

    @Override
    public String getEntityId(Source source) {
        return source.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "S";
    }
}

