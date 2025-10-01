package com.ada.genealogyapp.gedcom.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MoreTag {

    private String tag;

    private String value;

    private List<MoreTag> children;

}
