package com.ada.genealogyapp.citation.mapper.export.gedcom;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CitationGedcomMapper extends GedcomMapper<Citation> {

    @Override
    public String generateGedcomContent(Citation citation, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("0 ").append(gedcomId).append(" SOUR ").append(citation.getSource().getId()).append("\n");
        if (citation.getPage() != null) {
            builder.append("1 PAGE ").append(citation.getPage()).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String getEntityId(Citation citation) {
        return citation.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "C";
    }

    @Override
    public Class<Citation> getEntityType() {
        return Citation.class;
    }
}

