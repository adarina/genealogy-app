package com.ada.genealogyapp.source.mapper.export.json;

import com.ada.genealogyapp.export.json.JsonMapper;
import com.ada.genealogyapp.source.dto.SourceJsonRequest;
import com.ada.genealogyapp.source.model.Source;
import org.springframework.stereotype.Component;

@Component
public class SourceJsonMapper implements JsonMapper<Source, SourceJsonRequest> {

    @Override
    public SourceJsonRequest map(Source source) {
        return SourceJsonRequest.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }

    @Override
    public Class<Source> getEntityType() {
        return Source.class;
    }
}