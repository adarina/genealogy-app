package com.ada.genealogyapp.tree.dto.gedcom;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SourceCitation {

    private String ref;

    private String page;

    private String date;

    private String dataTagContents;

    private List<MediaRef> mediaRefs;
}
