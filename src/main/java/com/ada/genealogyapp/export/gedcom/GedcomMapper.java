package com.ada.genealogyapp.export.gedcom;

import com.ada.genealogyapp.export.dto.GedcomRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GedcomMapper<T> {

    public GedcomRequest map(Set<T> entities, Map<String, String> personGedcomIds) {
        StringBuilder gedcomBuilder = new StringBuilder();
        Map<String, String> entityGedcomIds = new HashMap<>();
        int counter = 1;

        for (T entity : entities) {
            String entityGedcomId = generateGedcomId(counter);
            entityGedcomIds.put(getEntityId(entity), entityGedcomId);
            gedcomBuilder.append(generateGedcomContent(entity, entityGedcomId, personGedcomIds));
            counter++;
        }
        return new GedcomRequest(gedcomBuilder.toString(), entityGedcomIds);
    }

    public abstract String generateGedcomContent(T entity, String gedcomId, Map<String, String> personGedcomIds);

    public abstract String getEntityId(T entity);

    public String generateGedcomId(int counter) {
        return "@" + getEntityTypePrefix() + counter + "@";
    }

    public abstract String getEntityTypePrefix();

    public abstract Class<T> getEntityType();
}

