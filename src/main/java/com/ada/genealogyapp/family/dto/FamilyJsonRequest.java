package com.ada.genealogyapp.family.dto;


import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class FamilyJsonRequest extends FamilyRequest {

    private String id;

    private String name;

    private String fatherId;

    private String motherId;

    private List<String> childrenIds = new ArrayList<>();

}
