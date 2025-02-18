package com.ada.genealogyapp.export.gedcom;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GedcomMapperFactory {

    private final Map<Class<?>, GedcomMapper<?>> mappers;

    public GedcomMapperFactory(Set<GedcomMapper<?>> mappersSet) {
        this.mappers = mappersSet.stream()
                .collect(Collectors.toMap(GedcomMapper::getEntityType, mapper -> mapper));
    }


    @SuppressWarnings("unchecked")
    public <E> GedcomMapper<E> getMapper(Class<E> entityType) {
        return (GedcomMapper<E>) mappers.get(entityType);
    }
}



