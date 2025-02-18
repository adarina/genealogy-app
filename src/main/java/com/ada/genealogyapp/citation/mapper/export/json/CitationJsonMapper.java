package com.ada.genealogyapp.citation.mapper.export.json;

import com.ada.genealogyapp.citation.dto.CitationJsonRequest;
import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.export.json.JsonMapper;
import org.springframework.stereotype.Component;
import com.ada.genealogyapp.file.model.File;


import java.util.stream.Collectors;

@Component
public class CitationJsonMapper implements JsonMapper<Citation, CitationJsonRequest> {

    @Override
    public CitationJsonRequest map(Citation citation) {
        return CitationJsonRequest.builder()
                .id(citation.getId())
                .page(citation.getPage())
                .date(citation.getDate())
                .sourceId(citation.getSource() != null ? citation.getSource().getId() : null)
                .filesIds(
                        citation.getFiles().stream()
                                .map(File::getId)
                                .collect(Collectors.toList())
                )
                .build();
    }
    @Override
    public Class<Citation> getEntityType() {
        return Citation.class;
    }
}

