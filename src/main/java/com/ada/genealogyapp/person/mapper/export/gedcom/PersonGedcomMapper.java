package com.ada.genealogyapp.person.mapper.export.gedcom;

import com.ada.genealogyapp.export.gedcom.GedcomMapper;
import com.ada.genealogyapp.person.model.Person;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PersonGedcomMapper extends GedcomMapper<Person> {

    @Override
    public Class<Person> getEntityType() {
        return Person.class;
    }

    @Override
    public String generateGedcomContent(Person person, String gedcomId, Map<String, String> personGedcomIds) {
        StringBuilder builder = new StringBuilder();
        builder.append("0 ").append(gedcomId).append(" INDI\n")
                .append("1 NAME ").append(person.getFirstname()).append(" /").append(person.getLastname()).append("/\n")
                .append("1 SEX ").append(person.getGender().name().substring(0, 1)).append("\n");
        return builder.toString();
    }

    @Override
    public String getEntityId(Person person) {
        return person.getId();
    }

    @Override
    public String getEntityTypePrefix() {
        return "P";
    }
}

