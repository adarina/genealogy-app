package com.ada.genealogyapp.family.mapper.export.json;

import com.ada.genealogyapp.family.dto.FamilyJsonRequest;
import com.ada.genealogyapp.family.model.Family;
import com.ada.genealogyapp.export.json.JsonMapper;
import org.springframework.stereotype.Component;
import com.ada.genealogyapp.participant.model.Participant;


import java.util.stream.Collectors;

@Component
public class FamilyJsonMapper implements JsonMapper<Family, FamilyJsonRequest> {

    @Override
    public FamilyJsonRequest map(Family family) {
        return FamilyJsonRequest.builder()
                .id(family.getId())
                .name(family.getName())
                .status(family.getStatus())
                .fatherId(family.getFather() != null ? family.getFather().getId() : null)
                .motherId(family.getMother() != null ? family.getMother().getId() : null)
                .childrenIds(
                        family.getChildren().stream()
                                .map(Participant::getId)
                                .collect(Collectors.toList())
                )
                .build();
    }

    @Override
    public Class<Family> getEntityType() {
        return Family.class;
    }
}