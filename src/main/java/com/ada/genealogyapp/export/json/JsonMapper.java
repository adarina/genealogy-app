package com.ada.genealogyapp.export.json;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface JsonMapper<E, D> {
    D map(E entity);
    default List<D> map(Collection<E> entities) {
        return entities.stream().map(this::map).collect(Collectors.toList());
    }
    Class<E> getEntityType();
}