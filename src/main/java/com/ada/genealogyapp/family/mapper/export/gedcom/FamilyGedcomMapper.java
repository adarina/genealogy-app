package com.ada.genealogyapp.family.mapper.export.gedcom;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FamilyGedcomMapper extends GedcomMapper<Family> {

    @Override
    public String generateGedcomContent(Family family, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("0 ").append(gedcomId).append(" FAM\n");
        if (family.getFather() != null) {
            builder.append("1 HUSB ").append(personGedcomIds.get(family.getFather().getId())).append("\n");
        }
        if (family.getMother() != null) {
            builder.append("1 WIFE ").append(personGedcomIds.get(family.getMother().getId())).append("\n");
        }
        for (Person child : family.getChildren()) {
            builder.append("1 CHIL ").append(personGedcomIds.get(child.getId())).append("\n");
        }
        return builder.toString();
    }

    @Override
    public String getEntityId(Family family) {
        return family.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "F";
    }

    @Override
    public Class<Family> getEntityType() {
        return Family.class;
    }
}
