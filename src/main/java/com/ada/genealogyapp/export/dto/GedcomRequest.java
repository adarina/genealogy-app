package com.ada.genealogyapp.export.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class GedcomRequest {

    private final String gedcomString;

    private final Map<String, String> gedcomIds;

}
