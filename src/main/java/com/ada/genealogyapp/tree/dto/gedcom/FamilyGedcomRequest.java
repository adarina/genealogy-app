package com.ada.genealogyapp.tree.dto.gedcom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FamilyGedcomRequest {

    private String id;

    private List<Reference> husbandRefs;

    private List<Reference> wifeRefs;

    private List<Reference> childRefs;

    private List<EventFact> eventsFacts;

}

