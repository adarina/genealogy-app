package com.ada.genealogyapp.gedcom.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EventFact {

    private String value;

    private String tag;

    private String type;

    private String date;

    private String place;

    private Address addr;

    private List<SourceCitation> sourceCitations;
}