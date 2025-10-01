package com.ada.genealogyapp.tree.dto.params;

import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.file.model.File;
import com.ada.genealogyapp.participant.model.Participant;
import com.ada.genealogyapp.person.model.Person;
import com.ada.genealogyapp.source.model.Source;
import com.ada.genealogyapp.tree.model.Tree;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
@Builder
public class TreeImportGedcomParams {

    private Tree tree;

    private String userId;
    @Builder.Default
    private Map<String, Source> sourceMap = new HashMap<>();
    @Builder.Default
    private Map<String, File> fileMap = new HashMap<>();
    @Builder.Default
    private Map<String, Person> personMap = new HashMap<>();
    @Builder.Default
    private Map<String, Participant> participantMap = new HashMap<>();
    @Builder.Default
    private Map<String, Family> familyMap = new HashMap<>();
}
