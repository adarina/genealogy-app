package com.ada.genealogyapp.relationship;

import com.ada.genealogyapp.citation.model.Citation;
import com.ada.genealogyapp.event.model.Event;
import com.ada.genealogyapp.event.relationship.EventCitation;
import com.ada.genealogyapp.event.service.EventService;
import com.ada.genealogyapp.family.dto.FamilyChildRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.person.relationship.PersonRelationship;
import com.ada.genealogyapp.person.service.PersonService;
import com.ada.genealogyapp.person.type.PersonRelationshipType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class RelationshipManager {

    private final PersonService personService;
    private final EventService eventService;

    public void addParentChildRelationship(Person parent, Person child, PersonRelationshipType relationshipType) {
        if (nonNull(parent)) {
            PersonRelationship relationship = PersonRelationship.builder()
                    .child(child)
                    .relationship(relationshipType)
                    .build();
            parent.getRelationships().add(relationship);
            personService.savePerson(parent);
        }
    }

    public void removeParentChildRelationships(Family family, Person child) {
        removeParentChildRelationship(family.getFather(), child);
        removeParentChildRelationship(family.getMother(), child);
    }

    public void updateParentChildRelationships(Family family, Person child, FamilyChildRequest familyChildRequest) {
        updateParentChildRelationship(family.getFather(), child, familyChildRequest.getFatherRelationship());
        updateParentChildRelationship(family.getMother(), child, familyChildRequest.getMotherRelationship());
    }

    private void updateParentChildRelationship(Person parent, Person child, PersonRelationshipType relationship) {
        if (nonNull(parent)) {
            parent.getRelationships().removeIf(rel -> rel.getChild().getId().equals(child.getId()));
            if (nonNull(relationship)) {
                PersonRelationship personRelationship = PersonRelationship.builder()
                        .child(child)
                        .relationship(relationship)
                        .build();
                parent.getRelationships().add(personRelationship);
            }
            personService.savePerson(parent);
        }
    }

    public void removeParentChildRelationship(Person parent, Person child) {
        if (nonNull(parent)) {
            parent.getRelationships().removeIf(rel -> rel.getChild().getId().equals(child.getId()));
            personService.savePerson(parent);
        }
    }



    public void removeEventCitationRelationship(Event event, Citation citation) {
        if (nonNull(event)) {
            event.getCitations().removeIf(rel -> rel.getCitation().equals(citation));
            eventService.saveEvent(event);
        }
    }


    public void addEventCitationRelationship(Event event, Citation citation) {
        if (nonNull(event)) {
            EventCitation relationship = EventCitation.builder()
                    .citation(citation)
                    .build();
            event.getCitations().add(relationship);
            eventService.saveEvent(event);
        }
    }
}


