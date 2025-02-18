package com.ada.genealogyapp.export.json;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JsonMapperFactory {

    private final Map<Class<?>, JsonMapper<?, ?>> mappers;

    public JsonMapperFactory(Set<JsonMapper<?, ?>> mappersSet) {
        this.mappers = mappersSet.stream()
                .collect(Collectors.toMap(JsonMapper::getEntityType, mapper -> mapper));
    }
    @SuppressWarnings("unchecked")
    public <E, D> JsonMapper<E, D> getMapper(Class<E> entityType) {
        return (JsonMapper<E, D>) mappers.get(entityType);
    }
}