package com.ada.genealogyapp.tree.dto.gedcom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PersonGedcomRequest {

    private String id;

    private List<Name> names;

    private List<Famc> famc;

    private List<Fams> fams;

    private List<EventFact> eventsFacts;

    private Change chan;
}