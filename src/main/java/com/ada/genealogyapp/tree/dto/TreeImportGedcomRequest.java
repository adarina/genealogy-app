package com.ada.genealogyapp.tree.dto;


import com.ada.genealogyapp.tree.dto.gedcom.FamilyGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.FileGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.PersonGedcomRequest;
import com.ada.genealogyapp.tree.dto.gedcom.SourceGedcomRequest;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TreeImportGedcomRequest {

    private List<PersonGedcomRequest> people;

    private List<FamilyGedcomRequest> families;

    private List<SourceGedcomRequest> sources;

    private List<FileGedcomRequest> media;

}
