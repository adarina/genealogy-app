package com.ada.genealogyapp.gedcom.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Name {

    private String value;

    private String givn;

    private String surn;

    @JsonProperty("_type")
    private String type;

    private String typeTag;

}
