package com.ada.genealogyapp.family.dto;

import com.ada.genealogyapp.family.type.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FamilyExportResponse {

    private StatusType status;

    private String id;

    private String name;

    private String fatherId;

    private String motherId;

    private Set<String> childrenIds = new HashSet<>();
}
