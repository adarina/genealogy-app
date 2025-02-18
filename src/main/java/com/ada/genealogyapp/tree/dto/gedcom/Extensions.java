package com.ada.genealogyapp.tree.dto.gedcom;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Extensions {

    @JsonProperty("folg.more_tags")
    private List<MoreTag> moreTags;

}
