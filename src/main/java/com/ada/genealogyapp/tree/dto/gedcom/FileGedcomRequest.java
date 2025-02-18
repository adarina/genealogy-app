package com.ada.genealogyapp.tree.dto.gedcom;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class FileGedcomRequest {

    private String id;

    private String _file;

    private Extensions extensions;

}