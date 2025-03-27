package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.StatusType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class FamiliesResponse {

    private String id;

    private StatusType status;

    private String motherName;

    private String fatherName;

    private String motherId;

    private String fatherId;

    private String marriageDate;
}
